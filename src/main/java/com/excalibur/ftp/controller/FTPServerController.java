package com.excalibur.ftp.controller;

import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import com.excalibur.ftp.service.FTPServerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FTPServerController {

    @Autowired
    private FTPServerService ftpService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/user/{key}")
    public String postFile(@PathVariable(name = "key") String key,
                           @RequestParam(name = "file") MultipartFile file,
                           @RequestHeader(name = "token", required = false) String token) {
        String fileName = null;
        try {
            fileName = ftpService.createUserFile(key, file);
            return new ObjectMapper().writeValueAsString(new FTPStoreResult(true, fileName, key, null));
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof JsonProcessingException) {
                try {
                    ftpService.deleteFile(key, fileName);
                    return e.getMessage();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            } else {
                try {
                    List<String> message = new ArrayList<>();
                    message.add(e.getMessage());
                    return new ObjectMapper().writeValueAsString(new FTPStoreResult(false, fileName, key, message));
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/user/{key}",
            consumes = "multipart/form-data",
            produces = "application/json"
    )
    public String postFiles(@PathVariable(name = "key") String key,
                            @RequestParam(name = "files") MultipartFile files) {
        try {
            return new ObjectMapper().writeValueAsString(ftpService);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/{fileName}")
    public byte[] getFile(@PathVariable(name = "userId") String userId,
                          @PathVariable(name = "fileName") String fileName) {
        try {
            return ftpService.getUserFile(userId, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/{resource}")
    public byte[] getSystemImage(@PathVariable(name = "resource") String resource) {
        try {
            return ftpService.getSystemFile(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
