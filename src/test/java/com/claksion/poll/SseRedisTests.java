package com.claksion.poll;

import com.claksion.app.service.PollPubSubService;
import com.claksion.app.service.RedisMessageSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Slf4j
class SseRedisTests {

    @Test
    void contextLoads() {
        log.info("TEST-------------------");
    }



    @Autowired
    private RedisMessageSubscriber subscriber;

    @MockBean
    private SseEmitter mockEmitter;

    @Autowired
    private PollPubSubService publisher;

    @MockBean
    private StringRedisTemplate redisTemplate;

    @Test
    public void testPublish() {
        String testChannel = "testChannel";
        String testMessage = "Test message";

        // 메시지 발행 시뮬레이션
        publisher.publishMessage(testChannel, testMessage);

        // RedisTemplate을 통해 메시지가 발행되었는지 확인
        Mockito.verify(redisTemplate, Mockito.times(1)).convertAndSend(testChannel, testMessage);
    }
    @Test
    public void testSendSseEventsToClients() throws IOException {
        CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
        emitters.add(mockEmitter);

        // Subscriber가 사용하는 Emitter 리스트를 설정
        subscriber.setEmitters(emitters);

        // 메시지 생성
        String testMessage = "Test message";
        byte[] messageBody = testMessage.getBytes(StandardCharsets.UTF_8);
        byte[] channel = "testChannel".getBytes(StandardCharsets.UTF_8);
        Message message = new DefaultMessage(channel, messageBody);

        // 메시지 전송 시뮬레이션
        subscriber.onMessage(message, null);

        // SseEmitter가 올바르게 메시지를 전송했는지 검증
        verify(mockEmitter, times(1)).send("Test message");
    }

}
