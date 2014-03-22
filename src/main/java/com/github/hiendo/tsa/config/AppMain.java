package com.github.hiendo.tsa.config;

import org.springframework.boot.SpringApplication;


public class AppMain {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(AppConfiguration.class);
        app.run(args);
    }
}
