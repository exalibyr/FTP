package com.excalibur.ftp.controller;

import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.util.ApplicationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FTPServerController {

    @Autowired
    private FTPServerService ftpService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/user/{userId}")
    public String postFile(@PathVariable(name = "userId") String userId,
                           @RequestParam(name = "file") MultipartFile file,
                           @RequestHeader(name = "token", required = false) String token) {
        try {
            return new ObjectMapper().writeValueAsString(ftpService.createUserFile(userId, file));
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
