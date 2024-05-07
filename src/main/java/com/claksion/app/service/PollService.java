package com.claksion.app.service;

import com.claksion.app.data.entity.PollEntity;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.PollRepository;
import com.claksion.app.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PollService implements BaseService<Integer, PollEntity> {
    
    final PollRepository pollRepository;
    final StringRedisTemplate redisTemplate;

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

    public int isPollFinished() throws Exception {
        return pollRepository.isPollFinished();
    }

    public int selectPollId(PollEntity pollEntity) throws Exception {
        return pollRepository.selectPollId(pollEntity);
    }

    public int selectRecentlyAddedPollId() throws Exception {
        return pollRepository.selectRecentlyAddedPollId();
    }

    public List<PollEntity> selectFinishedPoll() throws Exception {
        return pollRepository.selectFinishedPoll();
    }

    public Long addToSet(int pollId, String userId) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.add("poll:completedUser:"+pollId, userId.toString());
    }

    public Boolean isMember(int pollId, String userId) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.isMember("poll:completedUser:"+pollId, userId.toString());
    }

    public void deleteKey(int pollId) {
        redisTemplate.delete("poll:completedUser:"+pollId);
    }

    public Set<String> findValues(int pollId, int contentId) {
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.members("poll:"+pollId+":content:"+contentId);
    }
}
