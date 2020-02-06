package com.excalibur.ftp.entity.response.body;

import java.util.List;

public class FTPStoreResult extends Result {

    private String userId;
    private String fileName;

    public FTPStoreResult(boolean success, String fileName, String userId, List<String> errors) {
        super(success, errors);
        this.fileName = fileName;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
