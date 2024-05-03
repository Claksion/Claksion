package com.claksion.app.service.chat;

import com.claksion.app.data.dto.enums.MessageType;
import com.claksion.app.data.dto.msg.Msg;
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

    // 2. Redis 에서 메시지가 발행(publish)되면, listener 가 해당 메시지를 읽어서 처리
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            if (publishMessage != null && publishMessage.startsWith("{") && publishMessage.endsWith("}")) {
                Msg roomMessage = objectMapper.readValue(publishMessage, Msg.class);

                if (MessageType.TALK.equals(roomMessage.getType())) {
                    messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getChannelId(), roomMessage.getMessage());
                }
            } else {
                log.error("Received message is not in valid JSON format: {}", publishMessage);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}