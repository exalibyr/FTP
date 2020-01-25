package com.excalibur.ftp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FTPUtils {

    private static String serverName;
    private static int serverPort;
    private static String userName;
    private static String userPass;
    private static String defaultAvatarName;
    private static final String ROOT_DIR = "/";


    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(stream);
            serverName = properties.getProperty("FTPServer.name");
            serverPort = Integer.parseInt(properties.getProperty("FTPServer.port"));
            userName = properties.getProperty("FTPServer.user.name");
            userPass = properties.getProperty("FTPServer.user.password");
            defaultAvatarName = properties.getProperty("FTPServer.system.default.avatar.name");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        FTPUtils.serverName = serverName;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        FTPUtils.serverPort = serverPort;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FTPUtils.userName = userName;
    }

    public static String getUserPass() {
        return userPass;
    }

    public static void setUserPass(String userPass) {
        FTPUtils.userPass = userPass;
    }

    public static String getDefaultAvatarName() {
        return defaultAvatarName;
    }

    public static void setDefaultAvatarName(String defaultAvatarName) {
        FTPUtils.defaultAvatarName = defaultAvatarName;
    }

    public static String getRootDir() {
        return ROOT_DIR;
    }
}
