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
import java.util.logging.LogManager;

public class ApplicationUtils {

    static {
        try (InputStream stream = new FileInputStream("src/main/resources/properties/logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Endpoint {
        blog
    }

    private static final TextEncryptor TEXT_ENCRYPTOR = Encryptors.text(
            EncryptionConfiguration.getPassword(),
            EncryptionConfiguration.getSalt()
    );

    public static TextEncryptor getEncryptor() {
        return TEXT_ENCRYPTOR;
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
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }

}
