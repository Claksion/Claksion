package com.claksion.controller;

import com.claksion.app.data.dto.msg.Msg;
import com.claksion.app.service.ChatRoomRepository;
import com.claksion.app.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(Msg message) {
        if (Msg.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getChannelId());
            message.setMessage(message.getSendid() + "님이 입장하셨습니다.");
        }
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getChannelId()), message);
    }
}