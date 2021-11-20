package com.excalibur.ftp.configuration.proxy;

import com.excalibur.ftp.configuration.RepositoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Deprecated
@Component
public class FTPServerConfigurationProxy {

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    public String getServerName() {
        return repositoryConfiguration.getHost();
    }

    public int getServerPort() {
        return repositoryConfiguration.getPort();
    }

    public String getUserName() {
        return repositoryConfiguration.getUser().getName();
    }

    public String getUserPass() {
        return repositoryConfiguration.getUser().getPassword();
    }

    public String getSystemDirectory() {
        return repositoryConfiguration.getStoragePath(RepositoryConfiguration.StorageTag.WEB) + "/system";
    }

    public String getRootDirectory() {
        return repositoryConfiguration.getRootPath();
    }

    public String getAvatarDirectory() {
        return repositoryConfiguration.getStoragePath(RepositoryConfiguration.StorageTag.WEB) + "/avatar";
    }
}
