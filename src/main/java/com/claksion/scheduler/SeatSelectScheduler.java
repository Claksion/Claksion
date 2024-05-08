package com.claksion.scheduler;

import com.claksion.app.service.SeatSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatSelectScheduler {

//    private final SeatSelectService seatSelectService;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @Scheduled(fixedDelay = 1000)
//    private void seatSelectScheduler() throws Exception {
//        // 스케줄러가 참고해야 하는 빈 자리 key 가져오기
//        Set<String> keys = redisTemplate.keys("seat:*");
//        log.info("keys: {}", keys);
//
//        for (String seatKey : keys) {
//            seatSelectService.publish(seatKey);
//        }
//    }




    private final AtomicBoolean stop = new AtomicBoolean(false);

    @Scheduled(fixedRate = 1000)
    public void executeTask() {
        if(stop.get()) {
            // Task execution logic here
            System.out.println("Task executed");
            stop.set(false); // Reset the stop flag
        }
    }

    public void startTask() {
        stop.set(true);
    }

    public void stopTask() {
        stop.set(false);
    }
}
