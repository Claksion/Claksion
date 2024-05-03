package com.claksion.app.service.chat;

import com.claksion.app.data.dto.msg.Msg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final RedisTemplate<String, Msg> redisTemplateMessage;
//    private final MessageRepository messageRepository;
//    private final MessageRoomRepository messageRoomRepository;

    // 대화 저장
    public void saveMessage(Msg messageDto) {
        // DB 저장
//        Message message = new Message(messageDto.getSender(), messageDto.getRoomId(), messageDto.getMessage());
//        messageRepository.save(message);
        // 1. 직렬화
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Msg.class));
        log.info(messageDto.toString());
        // 2. redis 저장
        redisTemplateMessage.opsForList().rightPush(messageDto.getChannelId(), messageDto);

        // 3. expire 을 이용해서, Key 를 만료시킬 수 있음
        redisTemplateMessage.expire(messageDto.getChannelId(), 1, TimeUnit.MINUTES);
    }

    // 6. 대화 조회 - Redis & DB
    public List<Msg> loadMessage(String roomId) {
        List<Msg> messageList = new ArrayList<>();

        // Redis 에서 해당 채팅방의 메시지 100개 가져오기
        List<Msg> redisMessageList = redisTemplateMessage.opsForList().range(roomId, 0, 99);
        if (redisMessageList != null) {
            messageList.addAll(redisMessageList);
        }

        // 4. Redis 에서 가져온 메시지가 없다면, DB 에서 메시지 100개 가져오기
//        if (redisMessageList == null || redisMessageList.isEmpty()) {
//            // 5.
//            List<Message> dbMessageList = messageRepository.findTop100ByRoomIdOrderByCreatedAtAsc(roomId);
//
//            for (Message message : dbMessageList) {
//                Msg messageDto = new Msg(message);
//                messageList.add(messageDto);
//                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(Message.class));      // 직렬화
//                redisTemplateMessage.opsForList().rightPush(roomId, messageDto);                                // redis 저장
//            }
//        } else {
//            // 7.
//            messageList.addAll(redisMessageList);
//        }

        return messageList;
    }
}