package com.claksion.app.data.dto.response;

import com.claksion.app.data.dto.request.ChatMessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

import static java.time.LocalDate.now;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetChatMessageResponse {
    private int roomId;
    private int senderId;
    private String message;
    private Instant timestamp;
    // ChatMessageRequest를 매개변수로 받는 생성자 추가
    public GetChatMessageResponse(ChatMessageRequest request) {
        this.roomId = request.getRoomId();
        this.senderId = request.getSenderId();
        this.message = request.getMessage();
        this.timestamp = Instant.now();
    }

    // 기존의 정적 팩토리 메소드도 유지
    public static GetChatMessageResponse fromChatMessageRequest(ChatMessageRequest request) {
        return GetChatMessageResponse.builder()
                .roomId(request.getRoomId())
                .senderId(request.getSenderId())
                .message(request.getMessage())
                .timestamp(Instant.now())
                .build();
    }
}
