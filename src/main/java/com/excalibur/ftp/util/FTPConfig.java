package com.excalibur.ftp.util;

public class FTPConfig {

    private String serverName;
    private int serverPort;
    private String userName;
    private String userPass;
    private String rootDir;
    private String defaultAvatarName;

    public FTPConfig() {
        this.serverName = FTPUtils.getServerName();
        this.serverPort = FTPUtils.getServerPort();
        this.userName = FTPUtils.getUserName();
        this.userPass = FTPUtils.getUserPass();
        this.rootDir = FTPUtils.getRootDir();
        this.defaultAvatarName = FTPUtils.getDefaultAvatarName();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getDefaultAvatarName() {
        return defaultAvatarName;
    }

    public void setDefaultAvatarName(String defaultAvatarName) {
        this.defaultAvatarName = defaultAvatarName;
    }
}
