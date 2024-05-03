package com.claksion.app.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    public void setEmitters(CopyOnWriteArrayList<SseEmitter> emitters) {
        this.emitters = emitters;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Message received: " + new String(message.getBody()));


        String data = new String(message.getBody());
        // 메시지를 모든 SseEmitter에 전달
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(data);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}
