package com.claksion.controller;

import com.claksion.app.data.dto.ClassMate;
import com.claksion.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserRestController {

    final private UserService userService;
    final RedisTemplate<String, Object> redisTemplate;

    final ExecutorService executor = Executors.newCachedThreadPool();

    @GetMapping("/get")
    public SseEmitter get(@RequestParam("classroomId") Integer classroomId) throws Exception {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        executor.execute(() -> {
            try {
                while (true) {
                    List<ClassMate> classMates = userService.getClassMates(classroomId);
                    Map<Integer, Boolean> onlineState = new HashMap<>();

                    for (ClassMate mate : classMates) {
                        int userId = mate.getId();
                        boolean isOnline = Boolean.TRUE.equals(redisTemplate.hasKey("loginUser:" + userId));
                        onlineState.put(userId, isOnline);
                    }
                    emitter.send(onlineState);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
