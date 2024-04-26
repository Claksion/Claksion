package com.claksion.app.data.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BaseState status;

    public enum BaseState {
        ACTIVE, INACTIVE
    };
}
