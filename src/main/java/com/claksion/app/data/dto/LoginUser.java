package com.claksion.app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "loginUser", timeToLive = 18000) // session ttl과 동일하게 맞춤 (5시간)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginUser {
    @Id
    private int userId;
}
