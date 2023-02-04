package com.excalibur.fileapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    //TODO:2)доработать CORS конфигурацю
    //TODO:1) разобраться с Connection refused: connect когда подгружается сразу несколько файлов на страницу
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
