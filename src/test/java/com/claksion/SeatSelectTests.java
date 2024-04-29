package com.claksion;

import com.claksion.app.service.SeatSelectService;
import com.claksion.app.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SeatSelectTests {
    @Autowired
    SeatSelectService seatSelectService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Test
    void 자리선택_100명중_1명만_성공() throws InterruptedException {
        final String seatId = "A01";
        final int people = 100;
        final int limitCount = 1;
        final CountDownLatch countDownLatch = new CountDownLatch(people);
        seatSelectService.setEventCount(seatId, limitCount);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, seatId)))
                .limit(people)
                .collect(Collectors.toList());
        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(5000); // 기프티콘 발급 스케줄러 작업 시간

        final long failEventPeople = seatSelectService.getSize(seatId);
        assertEquals(people - limitCount, failEventPeople); // output : 70 = 100 - 30
    }

    private class AddQueueWorker implements Runnable{
        private CountDownLatch countDownLatch;
        private String seatId;

        public AddQueueWorker(CountDownLatch countDownLatch, String seatId) {
            this.countDownLatch = countDownLatch;
            this.seatId = seatId;
        }

        @Override
        public void run() {
            seatSelectService.addQueue(seatId);
            countDownLatch.countDown();
        }
    }

    @Test
    void 자리선택_remove(){
        redisTemplate.opsForZSet().remove("A01", "Thread-1");
    }
}
