package com.claksion.controller;

import com.claksion.app.data.dto.msg.Msg;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    /**
     *  클라이언트가 /pub/hello 로 메시지를 발행한다.
     *
     *      /sub/channel/channelID     - 구독
     *      /pub/hello                 - 메시지 발생
     */
    @MessageMapping("/hello")
    public void message(Msg msg){
        simpMessageSendingOperations.convertAndSend("/sub/channel/"+msg.getChannelId(), msg);
    }
}
