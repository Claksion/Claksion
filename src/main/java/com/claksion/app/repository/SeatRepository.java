package com.claksion.app.repository;

import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.frame.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SeatRepository extends BaseRepository<Integer, SeatEntity> {
    void deleteByClassroomId(int classroomId);
    List<SeatEntity> selectByClassroomId(int classroomId);
    void updateUserId(UpdateSeatUserRequest request);
}
