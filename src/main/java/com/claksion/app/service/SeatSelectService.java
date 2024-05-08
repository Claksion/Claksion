package com.claksion.app.service;

import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatSelectService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SeatService seatService;
    private final UserService userService;

    public boolean addQueue(int classroomId, int seatId, int userId) {
        String seatRedisKey = "seat:"+classroomId+":"+seatId;

        // 이미 선택된 좌석이라면 대기열에 추가하지 않음
        Set<Object> completedSeat = redisTemplate.opsForSet().members("completedSeat:" + classroomId);
        if (completedSeat.contains(seatRedisKey)) {
            return false;
        }

        final long now = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(seatRedisKey, String.valueOf(userId), (int) now);
        log.info("{} 유저가 {} 자리를 선택했습니다. ({}초)", userId, seatId, now);

        return true;
    }

    public void publish(String seatKey) throws Exception {
        Set<Object> users = redisTemplate.opsForZSet().range(seatKey, 0, 0);
        log.info("users :: "+users.toString());
        Object userId = users.stream().toList().get(0);
        int classroomId = Integer.parseInt(seatKey.split(":")[1]);
        int seatId = Integer.parseInt(seatKey.split(":")[2]);

        UpdateSeatUserRequest request = new UpdateSeatUserRequest(seatId, Integer.parseInt((String) userId));
        seatService.modifyUserId(request);

        log.info("✅'{}'님이 {} 자리 선택에 성공했습니다!", userId, seatKey);

        String key = "seat:"+classroomId+":"+seatId;
        System.out.println(key);
        if (redisTemplate.hasKey(key)) {
            redisTemplate.rename(key, "selected:"+key);
        }
        redisTemplate.opsForSet().add("completedSeat:" + seatKey.split(":")[1], seatKey);
    }


    public Map<String, Object> getMemberScore(int classroomId, int seatId) {
        System.out.println("selected:seat:"+classroomId+":"+seatId);
        Set<ZSetOperations.TypedTuple<Object>> resultSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores("selected:seat:"+classroomId+":"+seatId, 0, -1);
        Map<String, Object> pollContentsWithCntAndRanks = new LinkedHashMap<>();

        System.out.println(resultSet.toString());

        int rank = 1;
        double lastScore = Double.POSITIVE_INFINITY;
        int sameScoreRank = rank;

        for (ZSetOperations.TypedTuple<Object> member : resultSet) {
            if (member.getScore() != lastScore) {
                sameScoreRank = rank;
            }
            pollContentsWithCntAndRanks.put(member.getValue().toString(),sameScoreRank);
            lastScore = member.getScore();
            rank++;
        }
        return pollContentsWithCntAndRanks;
    }
}






