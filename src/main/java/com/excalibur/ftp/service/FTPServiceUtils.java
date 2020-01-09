package com.excalibur.ftp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FTPServiceUtils {

    private static String serverName;
    private static int serverPort;
    private static String userName;
    private static String userPass;
    public static String defaultAvatarName;
    private static final String ROOT_DIR = "/";


    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(stream);
            serverName = properties.getProperty("FTPServer.name");
            serverPort = Integer.valueOf(properties.getProperty("FTPServer.port"));
            userName = properties.getProperty("FTPServer.user.name");
            userPass = properties.getProperty("FTPServer.user.password");
            defaultAvatarName = properties.getProperty("FTPServer.system.default.avatar.name");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
