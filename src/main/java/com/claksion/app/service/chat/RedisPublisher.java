package com.claksion.app.service.chat;

import com.claksion.app.data.dto.msg.Msg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic channelTopic;
    public void publish(ChannelTopic topic, Msg message){
        // Redis Topic 에 메시지 발행.  메시지를 발행 후, 대기 중이던 redis 구독 서비스(RedisSubscriber)가 메시지를 처리

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), jsonMessage);
        redisTemplate.opsForList().rightPush("history:" + channelTopic.getTopic(), message.toString());
    }
    public List<Object> getMessages(String channel) {
        return redisTemplate.opsForList().range("history:" + channel, 0, -1);
    }
}
