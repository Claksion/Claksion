package com.claksion.app.data.dto.msg;
import com.claksion.app.data.dto.enums.MessageType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Msg {
    private String sendid;
    private String message;
    private String ChannelId;
    private MessageType type;

    public enum MessageType {
        ENTER,  // 입장 메시지
        TALK    // 대화 메시지
    }
}