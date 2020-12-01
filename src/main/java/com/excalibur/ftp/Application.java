package com.excalibur.ftp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    //TODO:2)доработать CORS конфигурацю
    //TODO:1) разобраться с Connection refused: connect когда подгружается сразу несколько файлов на страницу
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
