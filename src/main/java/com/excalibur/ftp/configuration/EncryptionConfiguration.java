package com.excalibur.ftp.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EncryptionConfiguration {

    private static String password;
    private static String salt;
    private static Boolean isActive;

    static {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream("src/main/resources/properties/encryption.properties")) {
            properties.load(stream);
            password = properties.getProperty("encryption.password");
            salt = properties.getProperty("encryption.salt");
            isActive = Boolean.valueOf(properties.getProperty("encryption.active"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPassword() {
        return password;
    }

    public static String getSalt() {
        return salt;
    }

    public static Boolean getIsActive() {
        return isActive;
    }
}
