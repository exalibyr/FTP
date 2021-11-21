package com.excalibur.ftp.controller;

import com.excalibur.ftp.configuration.RepositoryConfiguration;
import com.excalibur.ftp.error.HttpError;
import com.excalibur.ftp.exception.FTPIntegrationClientException;
import com.excalibur.ftp.response.entity.DeleteResponseBody;
import com.excalibur.ftp.response.entity.ErrorResponseBody;
import com.excalibur.ftp.response.entity.StoreResponseBody;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.response.ResponseBuilder;
import com.excalibur.ftp.service.FileDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
public class FTPServerController {

    @Autowired
    private RepositoryConfiguration config;

    @Autowired
    private FileDataService fileService;

    @Autowired
    private FTPServerService ftpService;

    private String storagePath;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/user/{key}", produces = "application/json")
    public ResponseEntity<StoreResponseBody> postFile(@PathVariable(name = "key") String key,
                                                 @RequestParam(name = "mediaType", required = false) String mediaType,
                                                 @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                                 @RequestBody(required = false) byte[] file) throws Exception {
        if (multipartFile != null) {
            StoreResponseBody responseBody = this.ftpService.createUserFile(key, multipartFile.getContentType(), multipartFile.getBytes());
            return ResponseBuilder.buildStoreResponse(responseBody);
        } else if (file != null) {
            StoreResponseBody responseBody = this.ftpService.createUserFile(key, mediaType, file);
            return ResponseBuilder.buildStoreResponse(responseBody);
        } else {
            throw new MissingServletRequestParameterException("mediaType, file, body", "String, MultipartFile, byte[]");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{key}")
    public ResponseEntity<DeleteResponseBody> deleteFile(@PathVariable(name = "key") String key,
                                                         @RequestParam(name = "fileName") String filename) throws Exception {
        DeleteResponseBody responseBody = ftpService.deleteFile(key, filename);
        return ResponseBuilder.buildDeleteResponse(responseBody);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/{fileName}")
    public byte[] getFile(@PathVariable(name = "userId") String userId,
                          @PathVariable(name = "fileName") String fileName) throws Exception {
        return ftpService.getUserFile(userId, fileName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/{resource}")
    public byte[] getSystemImage(@PathVariable(name = "resource") String resource) throws Exception {
        return ftpService.getSystemFile(resource);
    }

    @ExceptionHandler(FTPIntegrationClientException.class)
    public ResponseEntity<HttpError> handleException(FTPIntegrationClientException e) {
        log.error("Client exception: " + e.getError(), e);
        return ResponseEntity.status(e.getError().getStatusCode()).body(e.getError());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleException(Exception e) {
        log.error("Internal exception: ", e);
        return ResponseBuilder.buildErrorResponse(e.getMessage());
    }

    private String getStoragePath() {
        if (storagePath == null) {
            storagePath = config.getStoragePath(RepositoryConfiguration.StorageTag.WEB);
        }
        return storagePath;
    }

}
