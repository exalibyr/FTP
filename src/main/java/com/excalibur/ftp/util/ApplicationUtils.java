package com.excalibur.ftp.util;

import com.excalibur.ftp.configuration.BlogAppConfiguration;
import com.excalibur.ftp.configuration.EncryptionConfiguration;
import com.excalibur.ftp.configuration.FTPServerConfiguration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class ApplicationUtils {

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

}
