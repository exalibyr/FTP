package com.excalibur.fileapi.controller;

import com.excalibur.fileapi.service.FTPServerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/service")
@Log4j2
public class ServiceController {

    @Autowired
    private FTPServerService ftpService;

    @GetMapping("/system/{resource}")
    public byte[] getSystemImage(@PathVariable(name = "resource") String resource) {
        try {
            return ftpService.getSystemFile(resource);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to get system file ", e);
            return null;
        }
    }

}
