package com.excalibur.fileapi.configuration.external;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("blog")
@Getter
@Setter
@NoArgsConstructor
@Component
public class BlogAppSettings {

    private String url;
    private String authorization;

}
