package com.claksion.app.repository;

import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.frame.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ClassroomRepository extends BaseRepository<Integer, ClassroomEntity> {
}
