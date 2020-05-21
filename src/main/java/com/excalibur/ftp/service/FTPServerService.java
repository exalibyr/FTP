package com.excalibur.ftp.service;

import com.excalibur.ftp.response.entity.DeleteResponseBody;
import com.excalibur.ftp.response.entity.StoreResponseBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FTPServerService {

    StoreResponseBody createUserFile(String key, String mediaType, byte[] body);

    StoreResponseBody createUserFile(String key, MultipartFile file);

    DeleteResponseBody deleteFile(String key, String fileName);

    byte[] getUserFile(String userId, String filename) throws Exception;

    byte[] getSystemFile(String resource) throws Exception;

}
