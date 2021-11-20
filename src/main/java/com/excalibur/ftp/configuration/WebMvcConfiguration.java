package com.excalibur.ftp.configuration;

import com.excalibur.ftp.interceptor.AuthWebInterceptor;
import com.excalibur.ftp.interceptor.LogWebInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Component
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthWebInterceptor authWebInterceptor;
    @Autowired
    private LogWebInterceptor logWebInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logWebInterceptor);
        registry.addInterceptor(authWebInterceptor);
    }

}
