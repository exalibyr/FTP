package com.excalibur.ftp.controller;

import com.excalibur.ftp.entity.response.DeleteResponseBody;
import com.excalibur.ftp.entity.response.StoreResponseBody;
import com.excalibur.ftp.repository.FTPServerRepository;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.util.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<StoreResponseBody> postFile(@PathVariable(name = "key") String key,
                                                      @RequestParam(name = "file") MultipartFile file,
                                                      @RequestHeader(name = "token", required = false) String token) {
        StoreResponseBody responseBody = ftpService.createUserFile(key, file);
        logger.log(Level.INFO, "FILE STORED", responseBody);
        return ResponseBuilder.buildStoreResponse(responseBody);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{key}")
    public ResponseEntity<DeleteResponseBody> deleteFile(@PathVariable(name = "key") String key,
                                                         @RequestParam(name = "fileName") String filename) {
        DeleteResponseBody responseBody = ftpService.deleteFile(key, filename);
        logger.log(Level.INFO, "FILE DELETED", responseBody);
        return ResponseBuilder.buildDeleteResponse(responseBody);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/{fileName}")
    public byte[] getFile(@PathVariable(name = "userId") String userId,
                          @PathVariable(name = "fileName") String fileName) {
        try {
            return ftpService.getUserFile(userId, fileName);
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
