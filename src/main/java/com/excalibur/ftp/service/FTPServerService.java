package com.excalibur.ftp.service;

import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public interface FTPServerService {

    byte[] retrieveFile(String dirName);

    byte[] retrieveFile(String dirName, String fileName);

    FTPStoreResult storeFile(String dirName, String fileName, byte[] fileContent);

    void deleteFile(String directory, String name);

    FTPStoreResult createUserFile(String userId, MultipartFile file) throws IOException;

}
