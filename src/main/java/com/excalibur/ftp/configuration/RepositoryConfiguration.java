package com.excalibur.ftp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Component
@ConfigurationProperties(value = "repository")
@EnableConfigurationProperties
@Setter
@Getter
public class RepositoryConfiguration {

    private String host;
    private Integer port;
    private String rootPath;
    private User user;
    private List<Storage> storages;

    @Getter
    @Setter
    public static class User {
        private String name;
        private String password;
    }

    @Getter
    @Setter
    public static class Storage {
        private String tag;
        private String path;
    }

    public String getStoragePath(StorageTag tag) {
        Assert.notNull(tag, "tag can't be null");
        for (Storage storage : storages) {
            if (tag.name().equals(storage.getTag())) {
                return storage.getPath();
            }
        }
        throw new RuntimeException("Couldn't find storage path with tag " + tag);
    }

    public enum StorageTag {
        WEB,
        LOCAL;
    }
}
