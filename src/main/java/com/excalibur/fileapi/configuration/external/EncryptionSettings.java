package com.excalibur.fileapi.configuration.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("encryption")
@NoArgsConstructor
@Getter
@Setter
@Component
public class EncryptionSettings {

    private String password;
    private String salt;
    private Boolean active;

}
