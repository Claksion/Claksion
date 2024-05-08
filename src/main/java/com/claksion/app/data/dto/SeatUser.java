package com.claksion.app.data.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class SeatUser {
    private String name;
    private String profileImg;
    private String time;
    private boolean result;
}
