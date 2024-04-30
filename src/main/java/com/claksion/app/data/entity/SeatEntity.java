package com.claksion.app.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SeatEntity extends BaseEntity {
    private int id;
    private String zone;
    private int number;
    private int userId;
    private int classroomId;
}
