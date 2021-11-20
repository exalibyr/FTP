package com.excalibur.ftp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "auth")
@EnableConfigurationProperties
@Setter
@Getter
public class AuthConfiguration {

    private String name;
    private String value;
    private boolean active;

}
