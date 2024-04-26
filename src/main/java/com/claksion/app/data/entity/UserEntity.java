package com.claksion.app.data.entity;

public class UserEntity extends BaseEntity {
    private int id;
    private String name;
    private UserType type;
    private int classroomId;

    public enum UserType {
        STUDENT, TEACHER
    };
}
