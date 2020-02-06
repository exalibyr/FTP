package com.excalibur.ftp.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BlogAppConfiguration {

    private static String url;
    private static String token;
    private static String createFileURN;
    private static String createFileMethod;

    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/properties/blogApp.properties")) {
            properties.load(stream);
            url = properties.getProperty("blog.url");
            token = properties.getProperty("blog.api.token");
            createFileURN = properties.getProperty("blog.file.create.urn");
            createFileMethod = properties.getProperty("blog.file.create.method");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCreateFileMethod() {
        return createFileMethod;
    }

    public static String getUrl() {
        return url;
    }

    public static String getCreateFileURI() {
        return url + createFileURN;
    }

    public static String getToken() {
        return token;
    }
}
