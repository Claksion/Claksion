package com.claksion.app.service;

import com.claksion.app.data.dto.enums.MessageType;
import com.claksion.app.data.dto.request.ChatMessageRequest;
import com.claksion.app.data.dto.response.GetChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageRequest roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);

            if (roomMessage.getType().equals(MessageType.TALK)) {
                GetChatMessageResponse chatMessageResponse = new GetChatMessageResponse(roomMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), chatMessageResponse);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}