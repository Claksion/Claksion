package com.claksion.app.service;


import com.claksion.app.data.entity.PollContentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RankingService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void addPollCnt(int pollId, Map<Integer, Integer> voteCnt) {
        voteCnt.forEach((pollContentId, cnt) -> {
            redisTemplate.opsForZSet().add("poll:ranking:"+pollId, String.valueOf(pollContentId), cnt);
        });
    }

    public Double incrementPollCnt(int pollId, int pollContentId) {
        return redisTemplate.opsForZSet().incrementScore("poll:ranking:"+pollId, String.valueOf(pollContentId), 1);
    }

    public Map<String, String> getAllPollContentsWithCntAndRanks(int pollId) {
        Set<ZSetOperations.TypedTuple<String>> resultSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores("poll:ranking:"+pollId, 0, -1);
        Map<String, String> pollContentsWithCntAndRanks = new LinkedHashMap<>();

        int rank = 1;
        double lastScore = Double.POSITIVE_INFINITY;
        int sameScoreRank = rank;

        for (ZSetOperations.TypedTuple<String> member : resultSet) {
            if (member.getScore() != lastScore) {
                sameScoreRank = rank;
            }
            pollContentsWithCntAndRanks.put(member.getValue(), member.getScore().intValue() + "," + sameScoreRank);
            lastScore = member.getScore();
            rank++;
        }
        return pollContentsWithCntAndRanks;
    }

    public int getPollContentCnt(int pollId, int pollContentId) {
        return (int)Math.round(redisTemplate.opsForZSet().score("poll:ranking:"+pollId, String.valueOf(pollContentId)));
    }
    public int getPollContentRank(int pollId, int pollContentId) {
        Set<ZSetOperations.TypedTuple<String>> resultSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores("poll:ranking:" + pollId, 0, -1);

        int rank = 0;
        double lastScore = Double.POSITIVE_INFINITY;
        int numSameScore = 0;

        for (ZSetOperations.TypedTuple<String> member : resultSet) {
            if (member.getScore() != lastScore) {
                rank += numSameScore + 1;
                numSameScore = 0;
            } else {
                numSameScore++;
            }
            if (member.getValue().equals(String.valueOf(pollContentId))) {
                return rank;
            }
            lastScore = member.getScore();
        }
        return -1;
    }
    public Boolean isResultSaved(int pollId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("poll:ranking:"+pollId));
    }
    public boolean deleteFinishedPoll(int pollId) {
        return redisTemplate.delete("poll:ranking:"+pollId);
    }

}