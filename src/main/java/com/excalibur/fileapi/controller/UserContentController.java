package com.excalibur.fileapi.controller;

import com.excalibur.fileapi.response.ResponseBuilder;
import com.excalibur.fileapi.response.entity.DeleteResponseBody;
import com.excalibur.fileapi.response.entity.StoreResponseBody;
import com.excalibur.fileapi.service.FTPServerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserContentController {

    @Autowired
    private FTPServerService ftpService;

//    @CrossOrigin
    @PostMapping(value = "/{key}", produces = "application/json")
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
            log.info("FILE STORED: {}", responseBody);
            return ResponseBuilder.buildStoreResponse(responseBody);
        } catch (Exception e) {
            log.error("Failed to store file ", e);
            return ResponseBuilder.buildErrorResponse(e.getMessage());
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<DeleteResponseBody> deleteFile(@PathVariable(name = "key") String key,
                                                         @RequestParam(name = "fileName") String filename) {
        DeleteResponseBody responseBody = ftpService.deleteFile(key, filename);
        log.info("FILE DELETED: {}", responseBody);
        return ResponseBuilder.buildDeleteResponse(responseBody);
    }

    @GetMapping("/{key}/{fileName}")
    public byte[] getFile(@PathVariable(name = "key") String key,
                          @PathVariable(name = "fileName") String fileName) {
        try {
            return ftpService.getUserFile(key, fileName);
        } catch (Exception e) {
            log.error("Failed to get file {} ", fileName, e);
            return null;
        }
    }

}
