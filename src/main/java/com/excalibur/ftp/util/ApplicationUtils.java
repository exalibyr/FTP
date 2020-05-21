package com.excalibur.ftp.util;

import com.excalibur.ftp.configuration.BlogAppConfiguration;
import com.excalibur.ftp.configuration.EncryptionConfiguration;
import com.excalibur.ftp.configuration.FTPServerConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

public class ApplicationUtils {

    private static Map<String, String> fileExtensionByContentTypeMap = new HashMap<>();
    private static TextEncryptor textEncryptor;

    static {
        fileExtensionByContentTypeMap.put("image/jpeg", ".jpg");
        fileExtensionByContentTypeMap.put("image/png", ".png");
        if (EncryptionConfiguration.getIsActive()) {
            textEncryptor = Encryptors.text(
                    EncryptionConfiguration.getPassword(),
                    EncryptionConfiguration.getSalt()
            );
        } else {
            textEncryptor = Encryptors.noOpText();
        }
        try (InputStream stream = new FileInputStream("src/main/resources/properties/logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Endpoint {
        blog
    }

    public static TextEncryptor getEncryptor() {
        return textEncryptor;
    }

    public static String getApiKey(Endpoint endpoint) {
        switch (endpoint) {
            case blog: return getEncryptor().encrypt(BlogAppConfiguration.getToken());
            default: return null;
        }
    }

    public static boolean checkToken(Endpoint endpoint, String token) {
        switch (endpoint) {
            case blog: return BlogAppConfiguration.getToken().equals(getEncryptor().decrypt(token));
            default: return false;
        }
    }

    public static String getSystemDirectory(String name) {
        return FTPServerConfiguration.getSystemDirectory() + "/" + name;
    }

    public static String getSystemAvatarDir() {
        return FTPServerConfiguration.getSystemDirectory() + FTPServerConfiguration.getAvatarDirectory();
    }

    public static Boolean validateContentType(@Nullable String contentType) {
        return contentType != null && fileExtensionByContentTypeMap.containsKey(contentType);
    }

    public static String generateFileExtension(String contentType) {
        return fileExtensionByContentTypeMap.get(contentType);
    }

}
