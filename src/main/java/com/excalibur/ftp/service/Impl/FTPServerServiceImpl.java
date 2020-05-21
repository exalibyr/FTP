package com.excalibur.ftp.service.Impl;

import com.excalibur.ftp.response.entity.DeleteResponseBody;
import com.excalibur.ftp.response.entity.StoreResponseBody;
import com.excalibur.ftp.repository.FTPServerRepository;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.util.ApplicationUtils;
import com.excalibur.ftp.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.*;
import java.util.logging.Logger;

@Service
public class FTPServerServiceImpl implements FTPServerService {

    private Logger logger = Logger.getLogger(FTPServerServiceImpl.class.getName());

    @Autowired
    private FTPServerRepository ftpServerRepository;

    @Override
    public StoreResponseBody createUserFile(String key, String mediaType, byte[] body) {
        try {
            if ( !ApplicationUtils.validateContentType(mediaType)) {
                return new StoreResponseBody(
                        false,
                        null,
                        key,
                        mediaType + " is not a supported content type"
                );
            }

            String directoryName = ApplicationUtils.getEncryptor().decrypt(key);
            String extension = ApplicationUtils.generateFileExtension(mediaType);
            UUID uuid = UUIDGenerator.generateType5UUID(UUID.randomUUID().toString(), directoryName);
            String generatedFilename = uuid.toString() + extension;

            ftpServerRepository.storeFile("user/" + directoryName, generatedFilename, body);
            return new StoreResponseBody(
                    true,
                    generatedFilename,
                    ApplicationUtils.getEncryptor().encrypt(directoryName),
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new StoreResponseBody(
                    false,
                    null,
                    key,
                    e.getMessage()
            );
        }
    }

    @Override
    public StoreResponseBody createUserFile(String key, MultipartFile file) {
        try {
            if ( !ApplicationUtils.validateContentType(file.getContentType())) {
                return new StoreResponseBody(
                        false,
                        null,
                        key,
                        file.getContentType() + " is not a supported content type"
                );
            }

            String directoryName = ApplicationUtils.getEncryptor().decrypt(key);
            String extension = ApplicationUtils.generateFileExtension(file.getContentType());
            UUID uuid = UUIDGenerator.generateType5UUID(UUID.randomUUID().toString(), directoryName);
            String generatedFilename = uuid.toString() + extension;

            ftpServerRepository.storeFile("user/" + directoryName, generatedFilename, file.getBytes());
            return new StoreResponseBody(
                    true,
                    generatedFilename,
                    ApplicationUtils.getEncryptor().encrypt(directoryName),
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new StoreResponseBody(
                    false,
                    null,
                    key,
                    e.getMessage()
            );
        }
    }

    @Override
    public DeleteResponseBody deleteFile(String key, String fileName) {
        try {
            String directory = ApplicationUtils.getEncryptor().decrypt(key);
            ftpServerRepository.deleteFile("user/" + directory, fileName);
            return new DeleteResponseBody(
                    true,
                    null
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new DeleteResponseBody(
                    false,
                    e.getMessage()
            );
        }
    }

    @Override
    public byte[] getUserFile(String userId, String filename) throws Exception {
        return ftpServerRepository.retrieveFile("user/" + ApplicationUtils.getEncryptor().decrypt(userId), filename);
    }

    @Override
    public byte[] getSystemFile(String resource) throws Exception {
        return ftpServerRepository.retrieveFile(ApplicationUtils.getSystemDirectory(resource));
    }
}
