package com.claksion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
//@EnableScheduling
public class ClaksionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClaksionApplication.class, args);
    }

}
