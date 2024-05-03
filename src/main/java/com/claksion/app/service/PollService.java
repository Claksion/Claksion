package com.claksion.app.service;

import com.claksion.app.data.entity.PollEntity;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.PollRepository;
import com.claksion.app.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService implements BaseService<Integer, PollEntity> {
    
    final PollRepository pollRepository;
    
    @Override
    public int add(PollEntity pollEntity) throws Exception {
        return pollRepository.insert(pollEntity);
    }

    @Override
    public int del(Integer integer) throws Exception {
        return pollRepository.delete(integer);
    }

    @Override
    public int modify(PollEntity pollEntity) throws Exception {
        return pollRepository.update(pollEntity);
    }

    @Override
    public PollEntity get(Integer integer) throws Exception {
        return pollRepository.selectOne(integer);
    }

    @Override
    public List<PollEntity> get() throws Exception {
        return pollRepository.select();
    }

    public int selectPollId(PollEntity pollEntity) throws Exception {
        return pollRepository.selectPollId(pollEntity);
    }


}
