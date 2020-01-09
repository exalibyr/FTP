package com.excalibur.ftp.dao.response.body;

import java.util.List;

public class FTPStoreResult extends Result {

    private String key;

    public FTPStoreResult(boolean success, String key, List<String> errors) {
        super(success, errors);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
