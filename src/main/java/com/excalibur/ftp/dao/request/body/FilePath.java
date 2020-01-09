package com.excalibur.ftp.dao.request.body;

public class FilePath {

    private String directory;
    private String name;

    public FilePath() {
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
