package com.excalibur.ftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application {

    //TODO:2)доработать CORS конфигурацю
    //TODO:1) разобраться с completePendingCommand()
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
