package com.claksion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class ClaksionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClaksionApplication.class, args);
    }

}
