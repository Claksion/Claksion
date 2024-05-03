package com.claksion.app.service.chat;

import com.claksion.app.data.dto.msg.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisMessageStorage {

    private final RedisTemplate<String, Msg> redisTemplate;

    public void saveMessage(String room, Msg message) {
        ListOperations<String, Msg> listOps = redisTemplate.opsForList();
        listOps.leftPush("chat_room:" + room, message);
    }

    public List<Msg> getMessageHistory(String room) {
        return redisTemplate.opsForList().range("chat_room:" + room, 0, -1);
    }

}