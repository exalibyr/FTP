package com.excalibur.ftp.util;

import com.excalibur.ftp.configuration.BlogAppConfiguration;
import com.excalibur.ftp.configuration.EncryptionConfiguration;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import java.util.HashMap;
import java.util.Map;

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

    public static Boolean validateContentType(@Nullable String contentType) {
        return contentType != null && fileExtensionByContentTypeMap.containsKey(contentType);
    }

    public static String generateFileExtension(String contentType) {
        return fileExtensionByContentTypeMap.get(contentType);
    }

}
