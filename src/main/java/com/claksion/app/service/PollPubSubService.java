package com.claksion.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollPubSubService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisMessageListenerContainer redisContainer;

    @Autowired
    private RedisMessageSubscriber messageSubscriber;

    public void publishMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
//        redisTemplate.convertAndSend("ch:hayoung", "hi");
//        redisTemplate.convertAndSend("ch:hayoung", "hello");
    }

    public void subscribeChannel(String key) {
        redisContainer.addMessageListener(messageSubscriber, new ChannelTopic(key));
//        redisContainer.addMessageListener(messageSubscriber, new ChannelTopic("ch:hayoung"));
    }
}
