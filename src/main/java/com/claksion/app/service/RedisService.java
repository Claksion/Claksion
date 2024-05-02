package com.claksion.app.service;

import redis.clients.jedis.Jedis;

public class RedisService {
    public void reserveSeat(String userId) {
        // MyBatis를 사용하여 데이터베이스에 예약 정보 저장 로직
        // 예시 코드는 생략

        // Redis에 사용자 ID 저장
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            jedis.set("lastReservedBy", userId);
        }
    }
}


