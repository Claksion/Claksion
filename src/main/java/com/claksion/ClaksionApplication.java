package com.claksion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession()
//@EnableScheduling
public class ClaksionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClaksionApplication.class, args);
    }

}
