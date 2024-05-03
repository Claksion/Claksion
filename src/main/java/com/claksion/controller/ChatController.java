package com.claksion.controller;

import com.claksion.app.data.dto.msg.Msg;
import com.claksion.app.service.chat.ChatRoomRepository;
import com.claksion.app.service.chat.MessageService;
import com.claksion.app.service.chat.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChannelTopic channelTopic;

    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
//    @MessageMapping("/chat/message")
//    public void message(Msg message) {
//        if (Msg.MessageType.ENTER.equals(message.getType())) {
//            chatRoomRepository.enterChatRoom(message.getChannelId());
//            message.setMessage(message.getSendid() + "님이 입장하셨습니다.");
//        }
//        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//        redisPublisher.publish(chatRoomRepository.getTopic(message.getChannelId()), message);
//    }

    @MessageMapping("/chat/send")// 경로를 "/pub/chat/send"로 변경해야 할 수 있음
    public void sendMessage(Msg message) {
        String id = message.getSendid();
        String target = message.getMessage();
        log.info("Sender ID: {}, Target message: {}", id, target);
        template.convertAndSend("/sub/chat/room/1",message);
        // channelTopic = chatroom
        log.info("ChannelTopic is: {}", channelTopic);
        redisPublisher.publish(channelTopic, message);
    }
    // 대화 & 대화 저장
    @MessageMapping("/message")     // 1.
    public void message(Msg messageDto) {
//        // 클라이언트의 쪽지방(topic) 입장, 대화를 위해 리스너와 연동
//        messageRoomService.enterMessageRoom(messageDto.getRoomId());
        // Websocket 에 발행된 메시지를 redis 로 발행. 해당 쪽지방을 구독한 클라이언트에게 메시지가 실시간 전송됨 (1:N, 1:1 에서 사용 가능)
        redisPublisher.publish(channelTopic, messageDto);
        // DB & Redis 에 대화 저장
        messageService.saveMessage(messageDto);
    }

    // 대화 내역 조회
    @GetMapping("/api/room/{roomId}/message")
    public ResponseEntity<List<Msg>> loadMessage(@PathVariable String roomId) {
        return ResponseEntity.ok(messageService.loadMessage(roomId));
    }

    @GetMapping("/api/messages")
    public ResponseEntity<List<Object>> getMessages(@RequestParam("channel") String channel) {
        log.info("FFFFFFFFFFFFFFFFFFFFFFF");
        List<Object> messages = redisTemplate.opsForList().range("history:" + channel, 0, -1);
        log.info(">>>>>>>>>"+redisTemplate.opsForList().range("history:" + channel, 0, -1));
        return ResponseEntity.ok(messages);
    }

}