package com.claksion.controller;

import com.claksion.app.data.dto.msg.Msg;
import com.claksion.app.service.chat.ChatRoomRepository;
import com.claksion.app.service.chat.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChannelTopic channelTopic;

    private final SimpMessagingTemplate template;

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

    @MessageMapping("/chat/send")// 경로를 "/pub/chat/send"로 변경해야 할 수 있음
    public void sendMessage(Msg message) {
        String id = message.getSendid();
        String target = message.getMessage();
        log.info(id,target);
        template.convertAndSend("/sub/chat/room/1",message);
        log.info(">>>>>>>>>>>>>>");
        redisPublisher.publish(channelTopic, message);
    }
//    @MessageMapping("/chat/send")
//    @SendTo("/topic/messages")
//    public Msg sendMessage(Msg chatMessage) {
//        // Optionally save the message to Redis here
//        return chatMessage;
//    }


}