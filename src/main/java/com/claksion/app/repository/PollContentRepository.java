package com.claksion.app.repository;

import com.claksion.app.data.entity.PollContentEntity;
import com.claksion.app.frame.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PollContentRepository extends BaseRepository<Integer, PollContentEntity> {
    List<PollContentEntity> selectCheckedContents();

    List<PollContentEntity> selectPollContents(Integer integer);
}
