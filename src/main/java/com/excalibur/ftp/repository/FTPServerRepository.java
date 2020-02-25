package com.excalibur.ftp.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface FTPServerRepository {

    byte[] retrieveFile(String dirName) throws Exception;

    byte[] retrieveFile(String dirName, String fileName) throws Exception;

    void storeFile(String dirName, String fileName, byte[] fileContent) throws Exception;

    void deleteFile(String directory, String name) throws Exception;

}
