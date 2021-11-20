package com.excalibur.ftp.service;

import com.excalibur.ftp.converter.MimeToExtensionConverter;
import com.excalibur.ftp.exception.Exceptions;
import com.excalibur.ftp.exception.FTPIntegrationClientException;
import com.excalibur.ftp.model.FileData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
public class FileDataService implements FileInterface<FileData, String> {

    private static final String FILE_NAME_HEADER = "file_name";
    private static final String CONTENT_TYPE_HEADER = MessageHeaders.CONTENT_TYPE;

    @Autowired
    private FtpRemoteFileTemplate repository;

    @Autowired
    private MimeToExtensionConverter mimeToExtensionConverter;

    @Override
    public String upload(FileData file) throws FTPIntegrationClientException {
        log.info("Uploading new file: {}", file);
        Assert.notNull(file, "File data can't be null!");

        Map<String, Object> headers = new HashMap<>();
        headers.put(FILE_NAME_HEADER, generateFileName(file));
        headers.put(CONTENT_TYPE_HEADER, file.getContentType());

        String path = repository.send(new GenericMessage<>(file.getContent(), new MessageHeaders(headers)), file.getLocation(), FileExistsMode.IGNORE);
        if (path == null) {
            throw Exceptions.internalServerError("Failed to upload file: %s", file);
        } else {
            return path;
        }
    }

    @Override
    public FileData download(String path) throws FTPIntegrationClientException {
        log.info("Downloading file from: {}", path);
        Assert.notNull(path, "Path can't be null!");

        byte[] fileContent = repository.execute(session -> {
            try (ByteArrayOutputStream fileStream = new ByteArrayOutputStream()) {
                session.read(path, fileStream);
                return fileStream.toByteArray();
            } catch (IOException e) {
                log.error(String.format("Failed to read file from %s!", path), e);
                throw Exceptions.internalServerError();
            }
        });

        return FileData.builder().content(fileContent).path(path).build();
    }

    private String generateFileName(FileData fileData) {
        if (fileData.getExtension() == null) {
            return UUID.randomUUID() + "." + mimeToExtensionConverter.convert(fileData.getContentType());
        } else {
            return UUID.randomUUID() + "." + fileData.getExtension();
        }
    }

}
