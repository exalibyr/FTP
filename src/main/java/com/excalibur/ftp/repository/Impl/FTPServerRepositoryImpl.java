package com.excalibur.ftp.repository.Impl;

import com.excalibur.ftp.configuration.proxy.FTPServerConfigurationProxy;
import com.excalibur.ftp.repository.FTPServerRepository;
import com.excalibur.ftp.util.ApplicationUtils;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
@Repository
public class FTPServerRepositoryImpl implements FTPServerRepository {

    @Autowired
    private FTPServerConfigurationProxy ftpServerConfigurationProxy;

    private Logger logger = Logger.getLogger(FTPServerRepositoryImpl.class.getName());

    private FTPSClient ftpClient = new FTPSClient();

    @Override
    public byte[] retrieveFile(String dirName) throws Exception {
        if ( !ftpClient.isConnected()) restoreConnection();
        if (changeToRootDir()) {
            if (ftpClient.changeWorkingDirectory(dirName)) {
                return retrieveSingleFile();
            } else {
                throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
            }
        } else {
            throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + ftpServerConfigurationProxy.getRootDirectory() + " DIR FAILED");
        }
    }

    @Override
    public byte[] retrieveFile(String dirName, String fileName) throws Exception {
        if ( !ftpClient.isConnected()) restoreConnection();
        if (changeToRootDir()) {
            if (ftpClient.changeWorkingDirectory(dirName)) {
                return retrieveSingleFile(fileName);
            } else if (ftpClient.changeWorkingDirectory(ftpServerConfigurationProxy.getSystemDirectory() + ftpServerConfigurationProxy.getAvatarDirectory())) {
                return retrieveSingleFile();
            } else {
                throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
            }
        } else {
            throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + ftpServerConfigurationProxy.getRootDirectory() + " DIR FAILED");
        }
    }

    @Override
    public void deleteFile(String directory, String name) throws Exception {
        if ( !ftpClient.isConnected()) restoreConnection();
        if (changeToRootDir()) {
            if (ftpClient.changeWorkingDirectory(directory)) {
                if ( !ftpClient.deleteFile(name)) {
                    throw new IOException(ftpClient.printWorkingDirectory() + " - DELETE " + name + " FILE FAILED");
                }
            } else {
                throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + directory + " DIR FAILED");
            }
        } else {
            throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + ftpServerConfigurationProxy.getRootDirectory() + " DIR FAILED");
        }
    }

    @Override
    public void storeFile(String dirName, String fileName, byte[] fileContent) throws Exception {
        if ( !ftpClient.isConnected()) restoreConnection();
        if (changeToRootDir()) {
            if (ftpClient.changeWorkingDirectory(dirName)) {
                storeSingleFile(fileName, fileContent);
            } else {
                if (ftpClient.makeDirectory(ftpClient.printWorkingDirectory() + dirName)) {
                    if (ftpClient.changeWorkingDirectory(dirName)) {
                        storeSingleFile(fileName, fileContent);
                    } else {
                        throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                    }
                } else {
                    throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                }
            }
        } else {
            throw new IOException(ftpClient.printWorkingDirectory() + " - CHANGE TO " + ftpServerConfigurationProxy.getRootDirectory() + " DIR FAILED");
        }
    }

    private byte[] retrieveSingleFile(String fileName) throws Exception {
        try (InputStream stream = ftpClient.retrieveFileStream(fileName)) {
            byte[] content = stream.readAllBytes();
            if ( !ftpClient.completePendingCommand()) interruptTransaction();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new IOException(ftpClient.printWorkingDirectory() + " - RETRIEVE " + fileName + " FILE FAILED");
            logger.log(Level.SEVERE, "exception", e);
            //TODO: подумать как можно избавиться от бесконечного цикла и возвращать дефолтный файл
            return retrieveFile(ftpServerConfigurationProxy.getSystemDirectory() + ftpServerConfigurationProxy.getAvatarDirectory());
        }
    }

    private byte[] retrieveSingleFile() throws Exception {
        FTPFile[] files = ftpClient.mlistDir(ftpClient.printWorkingDirectory(), FTPFileFilters.NON_NULL);
        switch (files.length) {
            case 0 : throw new IOException(ftpClient.printWorkingDirectory() + " IS EMPTY");
            case 1 : return retrieveSingleFile(files[0].getName());
            default: {
                Map<Long, String> nameByTime = new TreeMap<>();
                for (FTPFile file : files) {
                    nameByTime.put(file.getTimestamp().getTimeInMillis(), file.getName());
                }
                return retrieveSingleFile(new LinkedList<>(nameByTime.values()).getLast());
            }
        }
    }

    private void storeSingleFile(String fileName, byte[] fileContent) throws Exception {
        try (InputStream stream = new ByteArrayInputStream(fileContent)) {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if ( !ftpClient.storeFile(fileName, stream)) {
                throw new IOException(ftpClient.printWorkingDirectory() + " - STORE " + fileName + " FILE FAILED");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "exception", e);
            throw new IOException(ftpClient.printWorkingDirectory() + " - STORE " + fileName + " FILE FAILED");
        }
    }

    private Boolean changeToRootDir() throws IOException{
        return ftpClient.changeWorkingDirectory(ftpServerConfigurationProxy.getRootDirectory());
    }

    private void startNewSession() throws IOException{

        ftpClient = new FTPSClient();
        ftpClient.connect(ftpServerConfigurationProxy.getServerName(), ftpServerConfigurationProxy.getServerPort());
        if (ftpClient.isConnected()) {
            if ( !ftpClient.login(ftpServerConfigurationProxy.getUserName(), ftpServerConfigurationProxy.getUserPass())) {
                throw new IOException("LOGIN FAILED");
            }
        } else {
            throw new IOException("CONNECT FAILED");
        }
    }

    private void restoreConnection() throws IOException{
        startNewSession();
    }

    private void interruptTransaction() throws IOException {
        ftpClient.abort();
        ftpClient.logout();
        ftpClient.disconnect();
        throw new IOException("TRANSACTION INTERRUPTED!!! DISCONNECTED");
    }

}
