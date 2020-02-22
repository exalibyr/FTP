package com.excalibur.ftp.service;

import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FTPServerService {

    String createUserFile(String key, MultipartFile file) throws Exception;

    void deleteFile(String key, String fileName) throws Exception;

    byte[] getUserFile(String userId, String filename) throws Exception;

    byte[] getSystemFile(String resource) throws Exception;

}
