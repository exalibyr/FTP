package com.excalibur.ftp.util;

import com.excalibur.ftp.configuration.EncryptionConfiguration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class ApplicationUtils {

    private static final TextEncryptor TEXT_ENCRYPTOR = Encryptors.text(
            EncryptionConfiguration.getPassword(),
            EncryptionConfiguration.getSalt()
    );

    public static TextEncryptor getEncryptor() {
        return TEXT_ENCRYPTOR;
    }

}
