package com.excalibur.ftp.dao.request.body;

public class File {

    private String userId;
    private String key;
    private String content;

    public File() {
    }

    public File(String userId, String key, String content) {
        this.userId = userId;
        this.key = key;
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
