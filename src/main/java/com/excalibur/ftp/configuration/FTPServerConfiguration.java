package com.excalibur.ftp.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FTPServerConfiguration {

    private static String serverName;
    private static int serverPort;
    private static String userName;
    private static String userPass;
    private static String defaultAvatarName;
    private static String defaultAvatarDirectory;
    private static String rootDirectory;

    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(stream);
            serverName = properties.getProperty("FTPServer.name");
            serverPort = Integer.parseInt(properties.getProperty("FTPServer.port"));
            userName = properties.getProperty("FTPServer.user.name");
            userPass = properties.getProperty("FTPServer.user.password");
            defaultAvatarName = properties.getProperty("FTPServer.system.default.avatar.name");
            defaultAvatarDirectory = properties.getProperty("FTPServer.system.default.avatar.directory");
            rootDirectory = properties.getProperty("FTPServer.directory.root.name");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        FTPServerConfiguration.serverName = serverName;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        FTPServerConfiguration.serverPort = serverPort;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FTPServerConfiguration.userName = userName;
    }

    public static String getUserPass() {
        return userPass;
    }

    public static void setUserPass(String userPass) {
        FTPServerConfiguration.userPass = userPass;
    }

    public static String getDefaultAvatarName() {
        return defaultAvatarName;
    }

    public static void setDefaultAvatarName(String defaultAvatarName) {
        FTPServerConfiguration.defaultAvatarName = defaultAvatarName;
    }

    public static String getRootDirectory() {
        return rootDirectory;
    }

    public static void setRootDirectory(String rootDirectory) {
        FTPServerConfiguration.rootDirectory = rootDirectory;
    }

    public static String getDefaultAvatarDirectory() {
        return defaultAvatarDirectory;
    }
}
