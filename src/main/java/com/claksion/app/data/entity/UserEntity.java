package com.claksion.app.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserEntity extends BaseEntity {
    private int id;
    private int classroomId;
    private String name;
    private UserType type;
    private String oauthId;
    private String profileImg;
    private String email;
}
