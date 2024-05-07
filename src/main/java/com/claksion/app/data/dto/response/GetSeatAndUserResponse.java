package com.claksion.app.data.dto.response;

import com.claksion.app.data.entity.BaseEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class GetSeatAndUserResponse extends BaseEntity {
    private int id;
    private String zone;
    private int number;
    private int userId;
    private int classroomId;
    private String userName;
}
