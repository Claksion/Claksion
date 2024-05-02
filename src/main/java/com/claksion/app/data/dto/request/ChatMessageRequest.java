package com.claksion.app.data.dto.request;

import com.claksion.app.data.dto.enums.MessageType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ChatMessageRequest {
    int roomId;
    int senderId;
    String message;
    MessageType type;
}
