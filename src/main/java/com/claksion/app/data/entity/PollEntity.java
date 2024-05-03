package com.claksion.app.data.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PollEntity extends BaseEntity {
    private int id;
    private int classroomId;
    private String title;
    private Boolean anonymity;
    private SelectType selectType;
    private LocalDateTime deadline;
    public enum SelectType {
        SINGLE, MULTI
    };
}
