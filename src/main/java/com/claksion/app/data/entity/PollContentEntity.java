package com.claksion.app.data.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PollContentEntity extends BaseEntity {
    private int id;
    private int pollId;
    private String name;
    private int cnt;
    private Boolean selected;
    private int ranking;
}
