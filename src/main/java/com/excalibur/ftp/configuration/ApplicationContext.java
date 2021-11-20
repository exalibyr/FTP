package com.excalibur.ftp.configuration;

import com.excalibur.ftp.controller.FTPIntegrationController;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.filters.AcceptAllFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.gateway.FtpOutboundGateway;
import org.springframework.integration.ftp.inbound.FtpStreamingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.messaging.Message;

@Configuration
@Log4j2
public class ApplicationContext {

    @Autowired
    private RepositoryConfiguration repositoryConfiguration;

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
        String host = repositoryConfiguration.getHost();
        log.info("Setting file storage host: {}", host);
        ftpSessionFactory.setHost(host);
        Integer port = repositoryConfiguration.getPort();
        log.info("Setting file storage port: {}", port);
        ftpSessionFactory.setPort(port);
        ftpSessionFactory.setClientMode(2);
        ftpSessionFactory.setFileType(FTP.BINARY_FILE_TYPE);
        ftpSessionFactory.setUsername(repositoryConfiguration.getUser().getName());
        ftpSessionFactory.setPassword(repositoryConfiguration.getUser().getPassword());
        ftpSessionFactory.setBufferSize(100000);
        return ftpSessionFactory;
    }

    @Bean
    public FtpRemoteFileTemplate ftpRemoteFileTemplate() {
        FtpRemoteFileTemplate ftpRemoteFileTemplate = new FtpRemoteFileTemplate(ftpSessionFactory());
        FtpRemoteFileTemplate.ExistsMode existsMode = FtpRemoteFileTemplate.ExistsMode.NLST_AND_DIRS;
        log.info("Setting exist mode for FTP client: {}", existsMode);
        ftpRemoteFileTemplate.setExistsMode(existsMode);
        ftpRemoteFileTemplate.setAutoCreateDirectory(true);
        ftpRemoteFileTemplate.setRemoteDirectoryExpression(new LiteralExpression(repositoryConfiguration.getRootPath()));
        return ftpRemoteFileTemplate;
    }

//    //producer
//    @Bean
//    @InboundChannelAdapter(channel = "fileTransferStream", poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "1"))
//    public MessageSource<InputStream> ftpMessageSource() {
//        FtpStreamingMessageSource messageSource = new FtpStreamingMessageSource(ftpRemoteFileTemplate());
//        String remoteDir = repositoryConfiguration.rootDir();
//        log.info("Setting remote directory: {}", remoteDir);
//        messageSource.setRemoteDirectory(remoteDir);
//        messageSource.setFilter(new AcceptAllFileListFilter<>());
//        return messageSource;
//    }
//
//    //producer/consumer
//    @Bean
//    @Transformer(inputChannel = "fileTransferStream", outputChannel = "fileData")
//    public org.springframework.integration.transformer.Transformer transformer() {
//        return new StreamTransformer("UTF-8");
//    }
//
//    //consumer
//    @ServiceActivator(inputChannel = "fileData")
//    @Bean
//    public MessageHandler handle() {
//        return message -> log.info("HEADERS: {}\nPAYLOAD: {}", message.getHeaders(), message.getPayload());
//    }

}
