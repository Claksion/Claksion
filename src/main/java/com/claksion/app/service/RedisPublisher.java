package com.claksion.app.service;

import com.claksion.app.data.dto.msg.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, Msg message){
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
