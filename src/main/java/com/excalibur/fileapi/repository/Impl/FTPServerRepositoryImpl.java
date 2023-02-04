package com.excalibur.fileapi.repository.Impl;

import com.excalibur.fileapi.configuration.external.FileServerSettings;
import com.excalibur.fileapi.ftp.FileClient;
import com.excalibur.fileapi.repository.FTPServerRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//TODO вынести управление сессией в отдельный класс
@Repository
@Log4j2
public class FTPServerRepositoryImpl implements FTPServerRepository {

    @Autowired
    private FileServerSettings fileServerSettings;

    @Autowired
    private FileClient fileClient;

    @Override
    public byte[] retrieveFile(String dirName) throws Exception {

        fileClient.startNewSession();
        if (changeToRootDir()) {

            if (fileClient.changeWorkingDirectory(dirName)) {

                byte[] file = retrieveSingleFile();
                fileClient.stopSession();
                return file;

            } else {
                fileClient.stopSession();
                throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
            }
        } else {
            fileClient.stopSession();
            throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + fileServerSettings.getPath().getRoot() + " DIR FAILED");
        }
    }

    @Override
    public byte[] retrieveFile(String dirName, String fileName) throws Exception {

        fileClient.startNewSession();
        if (changeToRootDir()) {

            if (fileClient.changeWorkingDirectory(dirName)) {

                byte[] file = retrieveSingleFile(fileName);
                fileClient.stopSession();
                return file;

            } else if (fileClient.changeWorkingDirectory(fileServerSettings.getPath().getSystem() + fileServerSettings.getPath().getAvatar())) {

                byte[] file = retrieveSingleFile();
                fileClient.stopSession();
                return file;

            } else {
                fileClient.stopSession();
                throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
            }
        } else {
            fileClient.stopSession();
            throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + fileServerSettings.getPath().getRoot() + " DIR FAILED");
        }
    }

    @Override
    public void deleteFile(String directory, String name) throws Exception {

        fileClient.startNewSession();
        if (changeToRootDir()) {

            if (fileClient.changeWorkingDirectory(directory)) {

                if ( !fileClient.deleteFile(name)) {
                    fileClient.stopSession();
                    throw new IOException(fileClient.printWorkingDirectory() + " - DELETE " + name + " FILE FAILED");
                }
            } else {
                fileClient.stopSession();
                throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + directory + " DIR FAILED");
            }
        } else {
            fileClient.stopSession();
            throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + fileServerSettings.getPath().getRoot() + " DIR FAILED");
        }
    }

    @Override
    public void storeFile(String dirName, String fileName, byte[] fileContent) throws Exception {

        fileClient.startNewSession();
        if (changeToRootDir()) {

            if (fileClient.changeWorkingDirectory(dirName)) {
                storeSingleFile(fileName, fileContent);
                fileClient.stopSession();
            } else {

                if (fileClient.makeDirectory(fileClient.printWorkingDirectory() + dirName)) {

                    if (fileClient.changeWorkingDirectory(dirName)) {
                        storeSingleFile(fileName, fileContent);
                        fileClient.stopSession();
                    } else {
                        fileClient.stopSession();
                        throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                    }
                } else {
                    fileClient.stopSession();
                    throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + dirName + " DIR FAILED");
                }
            }
        } else {
            fileClient.stopSession();
            throw new IOException(fileClient.printWorkingDirectory() + " - CHANGE TO " + fileServerSettings.getPath().getRoot() + " DIR FAILED");
        }
    }

    private byte[] retrieveSingleFile(String fileName) throws Exception {
        try (InputStream stream = fileClient.retrieveFileStream(fileName)) {
            byte[] content = stream.readAllBytes();
            if ( !fileClient.completePendingCommand()) interruptTransaction();
            return content;
        } catch (Exception e) {
//            throw new IOException(ftpClient.printWorkingDirectory() + " - RETRIEVE " + fileName + " FILE FAILED");
            log.error("Failed to retrieve file {} ", fileName, e);
            //TODO: подумать как можно избавиться от бесконечного цикла и возвращать дефолтный файл
            return retrieveFile(fileServerSettings.getPath().getSystem() + fileServerSettings.getPath().getAvatar());
        }
    }

    private byte[] retrieveSingleFile() throws Exception {
        FTPFile[] files = fileClient.mlistDir(fileClient.printWorkingDirectory(), FTPFileFilters.NON_NULL);
        switch (files.length) {
            case 0 : throw new IOException(fileClient.printWorkingDirectory() + " IS EMPTY");
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
            fileClient.enterLocalPassiveMode();
            fileClient.setFileType(FTP.BINARY_FILE_TYPE);
            if ( !fileClient.storeFile(fileName, stream)) {
                throw new IOException(fileClient.printWorkingDirectory() + " - STORE " + fileName + " FILE FAILED");
            }
        } catch (IOException e) {
            log.error("Failed to store file {} ", fileName, e);
            throw new IOException(fileClient.printWorkingDirectory() + " - STORE " + fileName + " FILE FAILED");
        }
    }

    private Boolean changeToRootDir() throws IOException{
        return fileClient.changeWorkingDirectory(fileServerSettings.getPath().getRoot());
    }


    private void interruptTransaction() throws IOException {
        fileClient.abort();
        fileClient.stopSession();
        throw new IOException("TRANSACTION INTERRUPTED!!! DISCONNECTED");
    }

}
