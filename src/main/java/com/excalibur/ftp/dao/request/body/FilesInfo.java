package com.excalibur.ftp.dao.request.body;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FilesInfo {

    private String userId;
    private Set<String> keys;

    public FilesInfo() {
    }

    public FilesInfo(String userId, Set<String> keys) {
        this.userId = userId;
        this.keys = keys;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }
}
