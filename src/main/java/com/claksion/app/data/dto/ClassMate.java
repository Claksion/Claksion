package com.claksion.app.data.dto;

import com.claksion.app.data.entity.UserType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class ClassMate {
    private int id;
    private String name;
    private UserType type;
    private String profileImg;
    private String email;
    private boolean online;
}
