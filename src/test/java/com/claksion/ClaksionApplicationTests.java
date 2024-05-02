package com.claksion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
class ClaksionApplicationTests {

    @Autowired
    private RedisTemplate<String, String> stringValueRedisTemplate;

    @Test
    void contextLoads() {
        log.info("TEST-------------------");
    }


}
