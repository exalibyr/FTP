package com.excalibur.ftp.service.Impl;

import com.excalibur.ftp.configuration.RepositoryConfiguration;
import com.excalibur.ftp.configuration.proxy.FTPServerConfigurationProxy;
import com.excalibur.ftp.model.FileData;
import com.excalibur.ftp.response.entity.DeleteResponseBody;
import com.excalibur.ftp.response.entity.StoreResponseBody;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.service.FileDataService;
import com.excalibur.ftp.util.ApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Deprecated
@Service
public class FTPServerServiceImpl implements FTPServerService {

    @Autowired
    private RepositoryConfiguration config;

    @Autowired
    private FileDataService fileService;

    @Autowired
    private FTPServerConfigurationProxy ftpServerConfigurationProxy;

    private String storagePath;

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

            FileData fileData = FileData.builder().contentType(mediaType).content(body)
                    .location(getStoragePath() + "/user/" + directoryName).build();

            String path = fileService.upload(fileData);

            return new StoreResponseBody(
                    true,
                    path.substring(path.lastIndexOf("/") + 1),
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

            FileData fileData = FileData.builder().content(file.getBytes()).contentType(file.getContentType())
                    .location(getStoragePath() + "/user/" + directoryName).build();

            String path = fileService.upload(fileData);

            return new StoreResponseBody(
                    true,
                    path.substring(path.lastIndexOf("/") + 1),
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

            fileService.delete(getStoragePath() + "/user/" + directory);

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
        String path = new StringBuilder()
                .append(getStoragePath())
                .append("/user/")
                .append(ApplicationUtils.getEncryptor().decrypt(userId))
                .append("/")
                .append(filename)
                .toString();

        FileData data = fileService.download(path);

        return data.getContent();
    }

    @Override
    public byte[] getSystemFile(String resource) throws Exception {
        String path = new StringBuilder()
                .append(ftpServerConfigurationProxy.getSystemDirectory())
                .append("/")
                .append(resource)
                .toString();

        FileData data = fileService.download(path);

        return data.getContent();

    }

    private String getStoragePath() {
        if (storagePath == null) {
            storagePath = config.getStoragePath(RepositoryConfiguration.StorageTag.WEB);
        }
        return storagePath;
    }

}
