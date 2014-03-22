package com.github.hiendo.tsa;

import com.github.hiendo.tsa.config.AppConfiguration;
import org.springframework.boot.SpringApplication;


public class DevAppMain {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(AppConfiguration.class);
        app.run(new String[]{"--debug"});
    }
}
