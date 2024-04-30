package com.claksion.scheduler;

import com.claksion.app.service.SeatSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatSelectScheduler {

    private final SeatSelectService seatSelectService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(fixedDelay = 1000)
    private void seatSelectScheduler() {
        // 스케줄러가 참고해야 하는 빈 자리 key 가져오기
        Set<String> keys = redisTemplate.keys("seat:*");
        log.info("* seat keys > " + keys.toString());

        for (String seatKey : keys) {
            seatSelectService.publish(seatKey);
        }
    }
}
