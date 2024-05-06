package com.claksion.app.data.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class SeatEntity extends BaseEntity {
    private int id;
    private String zone;
    private int number;
    private int userId;
    private int classroomId;
}
