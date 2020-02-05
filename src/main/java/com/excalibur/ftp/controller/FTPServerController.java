package com.excalibur.ftp.controller;

import com.excalibur.ftp.configuration.BlogAppConfiguration;
import com.excalibur.ftp.configuration.FTPServerConfiguration;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.service.FileWebService;
import com.excalibur.ftp.util.ApplicationUtils;
import com.excalibur.ftp.util.UUIDGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

//@CrossOrigin(methods = RequestMethod.POST, origins = BlogAppConfiguration.URL, allowCredentials = "true")
@RestController
public class FTPServerController {

    @Autowired
    private FileWebService fileWebService;

    @Autowired
    private FTPServerService ftpService;

//    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/avatar")
//    public byte[] getUserAvatar(@PathVariable(name = "userId") String userId) {
//        return ftpService.retrieveFile("user/" + userId + "/avatar");
//    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/user/{userId}")
    public String postFile(@PathVariable(name = "userId") String userId,
                                 @RequestParam(name = "file") MultipartFile file,
                                 @RequestHeader(name = "_csrf", required = false) String CSRF) {
        try {
//            storeResult = fileWebService.post(storeResult);
//            if (storeResult == null) {
//                return "error";
//            } else if (storeResult.isSuccess()) {
//                return "success";
//            } else {
//                ftpService.deleteFile("user/" + userId + "/avatar", storeResult.getFileName());
//                return "error";
//            }
            return new ObjectMapper().writeValueAsString(ftpService.createUserFile(userId, file));
        } catch (Exception e) {
            e.printStackTrace();
            return "error JSON composing";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/{fileName}")
    public byte[] getFile(@PathVariable(name = "userId") String userId,
                          @PathVariable(name = "fileName") String fileName) {
        return ftpService.retrieveFile("user/" + ApplicationUtils.getEncryptor().decrypt(userId), fileName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/avatar")
    public byte[] getDefaultAvatar() {
        return ftpService.retrieveFile(FTPServerConfiguration.getDefaultAvatarDirectory(), FTPServerConfiguration.getDefaultAvatarName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/welcome")
    public byte[] getWelcomeResource() {
        return ftpService.retrieveFile("/system/welcome");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/error")
    public byte[] getErrorResource() {
        return ftpService.retrieveFile("/system/error");
    }


//    @RequestMapping(method = RequestMethod.GET, value = "/services/rest/fileService/retrieveFile")
//    public byte[] retrieveFile(@RequestBody FilePath filePath) {
//        return ftpService.retrieveSingleFile(filePath.getDirectory(), filePath.getName());
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/services/rest/fileService/retrieveFiles")
//    public String retrieveFiles(@RequestBody FilesInfo filesInfo) {
//        FTPRetrieveResult retrieveResult = ftpService.retrieveFiles(filesInfo.getUserId(), filesInfo.getKeys());
//        try {
//            return new ObjectMapper().writeValueAsString(retrieveResult);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "FATAL ERROR: Couldn't compose JSON response";
//        }
//    }


}
