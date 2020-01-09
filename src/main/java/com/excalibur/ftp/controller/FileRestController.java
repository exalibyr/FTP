package com.excalibur.ftp.controller;

import com.excalibur.ftp.service.FTPServiceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.excalibur.ftp.dao.response.body.FTPStoreResult;
import com.excalibur.ftp.service.FTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class FileRestController {

    @Autowired
    private FTPService ftpService;

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/avatar")
    public byte[] getUserAvatar(@PathVariable(name = "userId") String userId) {
        return ftpService.retrieveFile("user/" + userId + "/avatar");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/{userId}/avatar/{fileName}")
    public byte[] getUserFile(@PathVariable(name = "userId") String userId, @PathVariable(name = "fileName") String fileName) {
        return ftpService.retrieveFile("user/" + userId + "/avatar", fileName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/system/avatar")
    public byte[] getDefaultAvatar() {
        return ftpService.retrieveFile("/system/avatar", FTPServiceUtils.defaultAvatarName);
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

    @RequestMapping(method = RequestMethod.POST, value = "/services/rest/fileService/saveUserAvatar")
    public String saveUserAvatar(@RequestParam(name = "userId") String userId,
                            @RequestParam(name = "fileName") String fileName,
                            @RequestBody byte[] fileContent) {
        FTPStoreResult storeResult = ftpService.storeFile("user/" + userId + "/avatar", fileName, fileContent);
        try {
            return new ObjectMapper().writeValueAsString(storeResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "FATAL ERROR: Couldn't compose JSON response";
        }
    }


}
