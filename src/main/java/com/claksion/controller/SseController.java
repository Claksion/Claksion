package com.claksion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping("/stream-sse")
    public SseEmitter streamSseMvc() {
        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }

    // 메시지를 보낼 수 있는 메소드 예시
    public void sendSseEventsToClients(String data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(data);
                Thread.sleep(1000);  // 메시지 간의 간격 설정
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
    }
}
