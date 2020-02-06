package com.excalibur.ftp.service;

import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FTPServerService {

    FTPStoreResult createUserFile(String userId, MultipartFile file) throws Exception;

    byte[] getUserFile(String userId, String filename) throws Exception;

    byte[] getSystemFile(String resource) throws Exception;

}
