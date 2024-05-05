package com.claksion.poll;

import com.claksion.app.service.RedisMessageSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class PollTests {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private RedisMessageSubscriber messageSubscriber;

//    @Autowired
//    private PollPubSubService pollPubSubService;

    @Test
    void contextLoads() {
        log.info("TEST-------------------");
    }

    @Test
    void pub_sub() throws InterruptedException {
        // publish
        redisTemplate.convertAndSend("ch:hayoung", "hi");

        // subscribe
        redisContainer.addMessageListener(messageSubscriber, new ChannelTopic("ch:hayoung"));

        // publish
        redisTemplate.convertAndSend("ch:hayoung", "hi");
        redisTemplate.convertAndSend("ch:hayoung", "hello");
        Thread.sleep(1000); // 기프티콘 발급 스케줄러 작업 시간
        redisTemplate.convertAndSend("ch:hayoung", "my");
        Thread.sleep(3000); // 기프티콘 발급 스케줄러 작업 시간
        redisTemplate.convertAndSend("ch:hayoung", "name");
        Thread.sleep(2000); // 기프티콘 발급 스케줄러 작업 시간
        redisTemplate.convertAndSend("ch:hayoung", "is");
        Thread.sleep(4000); // 기프티콘 발급 스케줄러 작업 시간
        redisTemplate.convertAndSend("ch:hayoung", "hayoung");

        // unsubscribe
        redisContainer.removeMessageListener(messageSubscriber, new ChannelTopic("ch:hayoung"));
    }

    @Test
    void 실시간_투표() {
//        // 1 증가
//        redisTemplate.opsForValue().increment("poll:"+"hi");
//        // 1 감소
//        redisTemplate.opsForValue().decrement("poll:"+"hi");

        ExecutorService executor = Executors.newFixedThreadPool(50);
        Random random = new Random();
        String[] keys = {"poll:1:test1", "poll:1:test2"};

        try {
            for (int i = 0; i < 50; i++) {
                executor.submit(() -> {
                    try {
                        String key = keys[random.nextInt(keys.length)];

                        redisTemplate.opsForValue().increment(key);
                        System.out.println(key+" 버튼 클릭");
                        System.out.println(redisTemplate.opsForValue().get(key));
//                        pollPubSubService.publishMessage(key, key.substring(7, 12));
                    } catch (Exception e) {
                        System.err.println("Error processing increment for key: " + e.getMessage());
                    }
                });
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }

        executor.shutdown(); // 모든 작업이 완료된 후 실행자 서비스 종료
    }

}
