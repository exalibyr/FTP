package com.excalibur.ftp.service.Impl;

import com.excalibur.ftp.configuration.FTPServerConfiguration;
import com.excalibur.ftp.entity.response.body.FTPStoreResult;
import com.excalibur.ftp.repository.FTPServerRepository;
import com.excalibur.ftp.service.FTPServerService;
import com.excalibur.ftp.util.ApplicationUtils;
import com.excalibur.ftp.util.UUIDGenerator;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class FTPServerServiceImpl implements FTPServerService {

    @Autowired
    private FTPServerRepository ftpServerRepository;

    @Override
    public FTPStoreResult createUserFile(String userId, MultipartFile file) throws Exception {
        String directoryName = ApplicationUtils.getEncryptor().decrypt(userId);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));

        UUID uuid = UUIDGenerator.generateType5UUID(UUID.randomUUID().toString(), directoryName);
        String generatedFilename = uuid.toString() + extension;
        try {
            ftpServerRepository.storeFile("user/" + directoryName, generatedFilename, file.getBytes());
            return new FTPStoreResult(true, generatedFilename, userId, null);
        } catch (IOException e) {
            e.printStackTrace();
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return new FTPStoreResult(false, generatedFilename, userId, errors);
        }
    }

    @Override
    public byte[] getUserFile(String userId, String filename) throws Exception {
        return ftpServerRepository.retrieveFile("user/" + ApplicationUtils.getEncryptor().decrypt(userId), filename);
    }

    @Override
    public byte[] getSystemFile(String resource) throws Exception {
        return ftpServerRepository.retrieveFile(ApplicationUtils.getSystemDirectory(resource));
    }
}
