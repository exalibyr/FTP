package com.excalibur.fileapi.configuration.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@ConfigurationProperties("file-server")
@Getter
@Setter
@NoArgsConstructor
@Component
public class FileServerSettings {

    private String hostname;
    private Integer port;

    @NestedConfigurationProperty
    private User user;

    @NestedConfigurationProperty
    private Path path;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class User {
        private String name;
        private String password;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Path {
        private String root;
        private String system;
        private String avatar;
    }

}
