package com.excalibur.ftp.controller;

import com.excalibur.ftp.response.entity.DeleteResponseBody;
import com.excalibur.ftp.response.entity.ResponseBody;
import com.excalibur.ftp.response.entity.StoreResponseBody;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class FTPServerController {

    private Logger logger = Logger.getLogger(FTPServerController.class.getName());

    @Autowired
    private FTPServerService ftpService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/user/{key}", produces = "application/json")
    public ResponseEntity postFile(@PathVariable(name = "key") String key,
                                                 @RequestParam(name = "mediaType", required = false) String mediaType,
                                                 @RequestParam(name = "file", required = false) MultipartFile multipartFile,
                                                 @RequestBody(required = false) byte[] file,
                                                 @RequestHeader(name = "token", required = false) String token) {
        StoreResponseBody responseBody = null;
        try {
            if (multipartFile != null) {
                responseBody = this.ftpService.createUserFile(key, multipartFile.getContentType(), multipartFile.getBytes());
            } else if (file.length > 0 && !mediaType.isBlank()) {
                responseBody = this.ftpService.createUserFile(key, mediaType, file);
            } else {
                throw new MissingServletRequestParameterException("mediaType, file, body", "String, MultipartFile, byte[]");
            }
            logger.log(Level.INFO, "FILE STORED", responseBody);
            return ResponseBuilder.buildStoreResponse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "exception", e);
            return ResponseBuilder.buildErrorResponse(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{key}")
    public ResponseEntity<DeleteResponseBody> deleteFile(@PathVariable(name = "key") String key,
                                                         @RequestParam(name = "fileName") String filename) {
        DeleteResponseBody responseBody = ftpService.deleteFile(key, filename);
        logger.log(Level.INFO, "FILE DELETED", responseBody);
        return ResponseBuilder.buildDeleteResponse(responseBody);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{key}/{fileName}")
    public byte[] getFile(@PathVariable(name = "key") String key,
                          @PathVariable(name = "fileName") String fileName) {
        try {
            return ftpService.getUserFile(key, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "exception", e);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/{resource}")
    public byte[] getSystemImage(@PathVariable(name = "resource") String resource) {
        try {
            return ftpService.getSystemFile(resource);
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "exception", e);
            return null;
        }
    }

}
