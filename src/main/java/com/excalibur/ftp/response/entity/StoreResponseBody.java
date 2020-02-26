package com.excalibur.ftp.response.entity;

public class StoreResponseBody extends ResponseBody {

    private String key;
    private String fileName;

    public StoreResponseBody(boolean success, String fileName, String key, String message) {
        super(success, message);
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
