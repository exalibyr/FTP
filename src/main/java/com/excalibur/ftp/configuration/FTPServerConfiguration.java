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
    private static String systemDirectory;
    private static String rootDirectory;
    private static String avatarDirectory;

    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(stream);
            serverName = properties.getProperty("FTPServer.name");
            serverPort = Integer.parseInt(properties.getProperty("FTPServer.port"));
            userName = properties.getProperty("FTPServer.user.name");
            userPass = properties.getProperty("FTPServer.user.password");
            rootDirectory = properties.getProperty("FTPServer.root.directory");
            systemDirectory = properties.getProperty("FTPServer.system.directory");
            avatarDirectory = properties.getProperty("FTPServer.avatar.directory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerName() {
        return serverName;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserPass() {
        return userPass;
    }

    public static String getSystemDirectory() {
        return systemDirectory;
    }

    public static String getRootDirectory() {
        return rootDirectory;
    }

    public static String getAvatarDirectory() {
        return avatarDirectory;
    }
}
