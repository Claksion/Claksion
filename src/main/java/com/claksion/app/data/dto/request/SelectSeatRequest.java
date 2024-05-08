package com.claksion.app.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SelectSeatRequest {
    private int seatId;
    private int classroomId;
    private int userId;
}
