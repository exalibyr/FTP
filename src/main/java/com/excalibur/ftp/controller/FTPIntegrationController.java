package com.excalibur.ftp.controller;

import com.excalibur.ftp.configuration.RepositoryConfiguration;
import com.excalibur.ftp.error.HttpError;
import com.excalibur.ftp.exception.Exceptions;
import com.excalibur.ftp.exception.FTPIntegrationClientException;
import com.excalibur.ftp.model.FileData;
import com.excalibur.ftp.service.FileDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@RestController
@RequestMapping(value = "v2/local/")
public class FTPIntegrationController {

    @Autowired
    private RepositoryConfiguration config;

    @Autowired
    private FileDataService fileService;

    private String storagePath;

    @PostMapping(value = "multipart-file")
    public String createMultipartFile(@RequestBody MultipartFile file) throws IOException {
        return fileService.upload(
                FileData
                        .builder()
                        .content(file.getBytes())
                        .location(getStoragePath())
                        .contentType(file.getContentType())
                        .build()
        );
    }

    @PostMapping(value = "binary-file")
    public String createBinaryFile(
            @RequestBody byte[] payload,
            @RequestParam(required = false) String contentType,
            @RequestParam(required = false) String extension
    ) {
        if (contentType == null && extension == null) {
            throw Exceptions.badRequest("Either contentType or extension required!");
        } else {
            return fileService.upload(
                    FileData
                            .builder()
                            .contentType(contentType)
                            .extension(extension)
                            .content(payload)
                            .location(getStoragePath())
                            .build()
            );
        }
    }

    @GetMapping(value = "file")
    public byte[] getFile(@RequestParam(name = "name") String fileName) {
        return fileService
                .download(String.format("/%s/%s", getStoragePath(), fileName))
                .getContent();
    }

    @ExceptionHandler(FTPIntegrationClientException.class)
    public ResponseEntity<HttpError> handleException(FTPIntegrationClientException e) {
        log.error("Client exception: " + e.getError(), e);
        return ResponseEntity.status(e.getError().getStatusCode()).body(e.getError());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpError> handleException(Exception e) {
        log.error("Internal exception: ", e);
        return ResponseEntity.internalServerError().body(new HttpError());
    }

    private String getStoragePath() {
        if (storagePath == null) {
            storagePath = config.getStoragePath(RepositoryConfiguration.StorageTag.LOCAL);
        }
        return storagePath;
    }

}
