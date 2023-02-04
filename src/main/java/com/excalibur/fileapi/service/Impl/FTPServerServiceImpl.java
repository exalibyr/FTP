package com.excalibur.fileapi.service.Impl;

import com.excalibur.fileapi.configuration.external.FileServerSettings;
import com.excalibur.fileapi.response.entity.DeleteResponseBody;
import com.excalibur.fileapi.response.entity.StoreResponseBody;
import com.excalibur.fileapi.repository.FTPServerRepository;
import com.excalibur.fileapi.security.PathEncryptor;
import com.excalibur.fileapi.service.FTPServerService;
import com.excalibur.fileapi.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
public class FTPServerServiceImpl implements FTPServerService {

    @Autowired
    private PathEncryptor pathEncryptor;

    @Autowired
    private FileServerSettings fileServerSettings;

    @Autowired
    private FTPServerRepository ftpServerRepository;

    @Override
    public StoreResponseBody createUserFile(String key, String mediaType, byte[] body) {
        try {

            String extension = MediaType.getExtension(mediaType);

            if (extension == null) {
                return new StoreResponseBody(
                        false,
                        null,
                        key,
                        mediaType + " is not a supported content type"
                );
            }

            String directoryName = pathEncryptor.decrypt(key);
            String generatedFilename = UUID.randomUUID() + extension;

            ftpServerRepository.storeFile("user/" + directoryName, generatedFilename, body);
            return new StoreResponseBody(
                    true,
                    generatedFilename,
                    pathEncryptor.encrypt(directoryName),
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

            String extension = MediaType.getExtension(file.getContentType());

            if (extension == null) {
                return new StoreResponseBody(
                        false,
                        null,
                        key,
                        file.getContentType() + " is not a supported content type"
                );
            }

            String directoryName = pathEncryptor.decrypt(key);
            String generatedFilename = UUID.randomUUID() + extension;

            ftpServerRepository.storeFile("user/" + directoryName, generatedFilename, file.getBytes());
            return new StoreResponseBody(
                    true,
                    generatedFilename,
                    pathEncryptor.encrypt(directoryName),
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
            String directory = pathEncryptor.decrypt(key);
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
    public byte[] getUserFile(String key, String filename) throws Exception {
        return ftpServerRepository.retrieveFile("user/" + pathEncryptor.decrypt(key), filename);
    }

    @Override
    public byte[] getSystemFile(String resource) throws Exception {
        return ftpServerRepository.retrieveFile(fileServerSettings.getPath().getSystem() + "/" + resource);
    }
}
