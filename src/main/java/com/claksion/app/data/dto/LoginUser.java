package com.claksion.app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "loginUser", timeToLive = 600) // redis에 존재하는 시간 = 600초
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginUser {
    @Id
    private int userId;
}
