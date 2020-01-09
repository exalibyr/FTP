package com.excalibur.ftp.service;

import com.excalibur.ftp.dao.response.body.FTPStoreResult;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilters;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class FTPService {

    private String serverName;
    private int serverPort;
    private String userName;
    private String userPass;
    private static final String ROOT_DIR = "/";
    private FTPClient ftpClient;
//    private ReentrantLock lock;
//    private FTPNoOpThread noOpThread;

    FTPService() {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(stream);
            serverName = properties.getProperty("FTPServer.name");
            serverPort = Integer.valueOf(properties.getProperty("FTPServer.port"));
            userName = properties.getProperty("FTPServer.user.name");
            userPass = properties.getProperty("FTPServer.user.password");
            startNewSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] retrieveFile(String dirName) {
        try {
//            lock.lock();
            if ( !ftpClient.isConnected()) restoreConnection();
            if (changeToRootDir()) {
                if (ftpClient.changeWorkingDirectory(dirName)) {
                    return retrieveSingleFile();
                } else {
                    throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                }
            } else {
                throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO ROOT DIR FAILED");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
//            lock.unlock();
        }
    }

    private byte[] retrieveSingleFile() throws IOException {
        FTPFile[] files = ftpClient.mlistDir(ftpClient.printWorkingDirectory(), FTPFileFilters.NON_NULL);
        switch (files.length) {
            case 0 : {
                throw new IOException(ftpClient.printWorkingDirectory() + " - AVATAR NOT FOUND");
            }
            case 1 : {
                return retrieveSingleFile(files[0].getName());
            }
            default: {
                Map<Long, String> nameByTime = new TreeMap<>();
                for (FTPFile file : files) {
                    nameByTime.put(file.getTimestamp().getTimeInMillis(), file.getName());
                }
                return retrieveSingleFile(new LinkedList<>(nameByTime.values()).getLast());
            }
        }
    }

    public byte[] retrieveFile(String dirName, String fileName) {
        try {
//            lock.lock();
            if ( !ftpClient.isConnected()) restoreConnection();
            if (changeToRootDir()) {
                if (ftpClient.changeWorkingDirectory(dirName)) {
                    return retrieveSingleFile(fileName);
                } else {
                    throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                }
            } else {
                throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO ROOT DIR FAILED");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
//            lock.unlock();
        }
    }

    private byte[] retrieveSingleFile(String fileName) throws IOException {
        InputStream stream = ftpClient.retrieveFileStream(fileName);
        if (stream == null) {
            throw new IOException(ftpClient.printWorkingDirectory() + " - RETRIEVE " + fileName + " FILE FAILED");
        } else {
            byte[] content = stream.readAllBytes();
            stream.close();
            if ( !ftpClient.completePendingCommand()) interruptTransaction();
            return content;
        }
    }

//    public FTPRetrieveResult retrieveFiles(String dirName, Set<String> fileNames) {
//        List<String> errors = new ArrayList<>();
//        try {
////            lock.lock();
//            if ( !ftpClient.isConnected()) restoreConnection();
//            if (changeToRootDir()) {
//                if (ftpClient.changeWorkingDirectory(dirName)) {
//                    Map<String, byte[]> nameContent = new HashMap<>();
//                    for (String fileName : fileNames) {
//                        ftpClient.enterLocalPassiveMode();
//                        InputStream stream = ftpClient.retrieveFileStream(fileName);
//                        if (stream == null) {
//                            errors.add("RETRIEVE " + fileName + " FILE FAILED");
//                            return new FTPRetrieveResult(false, errors, null);
//                        } else {
//                            nameContent.put(fileName, stream.readAllBytes());
//                        }
//                    }
//                    return new FTPRetrieveResult(true, errors, nameContent);
//                } else {
//                    errors.add("CHANGE TO " + dirName + " DIR FAILED");
//                    return new FTPRetrieveResult(false, errors, null);
//                }
//            } else {
//                errors.add("CHANGE TO ROOT DIR FAILED");
//                return new FTPRetrieveResult(false, errors, null);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            errors.add(ex.getMessage());
//            return new FTPRetrieveResult(false, errors, null);
//        } finally {
////            lock.unlock();
//        }
//    }

    public FTPStoreResult storeFile(String dirName, String fileName, byte[] fileContent) {
        List<String> errors = new ArrayList<>();
        try {
//            lock.lock();
            if ( !ftpClient.isConnected()) restoreConnection();
            if (changeToRootDir()) {
                if (ftpClient.changeWorkingDirectory(dirName)) {
                    return storeSingleFile(fileName, fileContent, errors);
                } else {
                    if (ftpClient.makeDirectory(ftpClient.printWorkingDirectory() + dirName)) {
                        if (ftpClient.changeWorkingDirectory(dirName)) {
                            return storeSingleFile(fileName, fileContent, errors);
                        } else {
                            errors.add(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                            return new FTPStoreResult(false, null, errors);
                        }
                    } else {
                        errors.add(ftpClient.printWorkingDirectory() + " - MAKE " + dirName + " DIR FAILED");
                        return new FTPStoreResult(false, null, errors);
                    }
                }
            } else {
                errors.add(ftpClient.printWorkingDirectory() + " - CHANGE TO ROOT DIR FAILED");
                return new FTPStoreResult(false, null, errors);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errors.add(ex.getMessage());
            return new FTPStoreResult(false, fileName, errors);
        } finally {
//            lock.unlock();
        }
    }


//   public FTPStoreResult storeSingleFile(String dirName, String fileName, byte[] fileContent) {
//       FTPClient ftpClient = new FTPClient();
//       List<String> errors = new ArrayList<>();
//       try {
//           ftpClient.connect(serverName, serverPort);
//           if (ftpClient.isConnected()) {
//               if (ftpClient.login(userName, userPass)) {
//                   ftpClient.enterLocalPassiveMode();
//                   if (ftpClient.changeWorkingDirectory(dirName)) {
//                       storeSingleFile(ftpClient, fileName, fileContent);
//                       return new FTPStoreResult(true, fileName, errors);
//                   } else {
//                       if (ftpClient.makeDirectory(ftpClient.printWorkingDirectory() + dirName)) {
//                           if (ftpClient.changeWorkingDirectory(dirName)) {
//                               storeSingleFile(ftpClient, fileName, fileContent);
//                               return new FTPStoreResult(true, fileName, errors);
//                           } else {
//                               errors.add("CHANGE TO " + dirName + " DIR FAILED");
//                               return new FTPStoreResult(false, fileName, errors);
//                           }
//                       } else {
//                           errors.add("MAKE " + dirName + " DIR FAILED");
//                           return new FTPStoreResult(false, fileName, errors);
//                       }
//                   }
//               } else {
//                   errors.add("LOGIN FAILED");
//                   return new FTPStoreResult(false, fileName, errors);
//               }
//           } else {
//               errors.add("CONNECT FAILED");
//               return new FTPStoreResult(false, fileName, errors);
//           }
//       } catch (Exception ex) {
//           ex.printStackTrace();
//           errors.add(ex.getMessage());
//           return new FTPStoreResult(false, fileName, errors);
//       } finally {
//           try {
//               ftpClient.abor();
//               ftpClient.quit();
//           } catch (Exception e) {
//               e.printStackTrace();
//           }
//       }
//   }

   private FTPStoreResult storeSingleFile(String fileName, byte[] fileContent, List<String> errors) throws IOException{
       ftpClient.enterLocalPassiveMode();
       InputStream stream = new ByteArrayInputStream(fileContent);
       ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
       if (ftpClient.storeFile(fileName, stream)) {
           stream.close();
           return new FTPStoreResult(true, fileName, errors);
       } else {
           stream.close();
           errors.add(ftpClient.printWorkingDirectory() + " - STORE " + fileName + " FILE FAILED");
           return new FTPStoreResult(false, null, errors);
       }
   }

   private Boolean changeToRootDir() throws IOException{
        return ftpClient.changeWorkingDirectory(ROOT_DIR);
   }

//   private void startNoOpThread() {
//       noOpThread = new FTPNoOpThread(ftpClient);
//       lock = noOpThread.getLock();
//       noOpThread.run();
//   }

   private void startNewSession() throws IOException {
       ftpClient = new FTPClient();
       ftpClient.connect(serverName, serverPort);
       if (ftpClient.isConnected()) {
           if (ftpClient.login(userName, userPass)) {
//               startNoOpThread();
           } else {
               throw new IOException("LOGIN FAILED");
           }
       } else {
           throw new IOException("CONNECT FAILED");
       }
   }

   private void restoreConnection() throws IOException{
//       noOpThread.setAlive(false);
       startNewSession();
   }

   private void interruptTransaction() throws IOException {
        ftpClient.abort();
        ftpClient.logout();
        ftpClient.disconnect();
        throw new IOException("TRANSACTION INTERRUPTED!!! DISCONNECTED");
   }



}
