package com.claksion.app.repository;

import com.claksion.app.data.entity.PollEntity;
import com.claksion.app.frame.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PollRepository extends BaseRepository<Integer, PollEntity> {
    int isPollFinished();
    int selectPollId(PollEntity pollEntity);

    int selectRecentlyAddedPollId();

    List<PollEntity> selectFinishedPoll();
}
