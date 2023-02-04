package com.excalibur.fileapi.security;

import com.excalibur.fileapi.configuration.external.EncryptionSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class PathEncryptor {
    private TextEncryptor encryptor;

    public PathEncryptor(@Autowired EncryptionSettings settings) {
        if (settings.getActive()) {
            encryptor = Encryptors.text(
                    settings.getPassword(),
                    settings.getSalt()
            );
        } else {
            encryptor = Encryptors.noOpText();
        }
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
