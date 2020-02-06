package com.excalibur.ftp.entity.response.body;

import java.util.List;

public class FTPStoreResult extends Result {

    private String key;
    private String fileName;

    public FTPStoreResult(boolean success, String fileName, String key, List<String> errors) {
        super(success, errors);
        this.fileName = fileName;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
