package com.claksion.app.service;

import com.claksion.app.data.dto.request.SelectSeatRequest;
import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import com.claksion.app.service.aop.AroundValidSeatOnRedis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatSelectService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SeatService seatService;

    private static int DIV_VALUE = 1000000; // 시간 계산용

    public Map<String, Object> getMemberScore(int classroomId, int seatId) {
        System.out.println("selected:seat:" + classroomId + ":" + seatId);
        Set<ZSetOperations.TypedTuple<Object>> resultSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores("selected:seat:" + classroomId + ":" + seatId, 0, -1);
        Map<String, Object> pollContentsWithCntAndRanks = new LinkedHashMap<>();

        System.out.println(resultSet.toString());

        int rank = 1;
        double lastScore = Double.POSITIVE_INFINITY;
        int sameScoreRank = rank;

        for (ZSetOperations.TypedTuple<Object> member : resultSet) {
            if (member.getScore() != lastScore) {
                sameScoreRank = rank;
            }
            pollContentsWithCntAndRanks.put(member.getValue().toString(), sameScoreRank);
            lastScore = member.getScore();
            rank++;
        }
        return pollContentsWithCntAndRanks;
    }

    @Transactional(rollbackFor = ValidationFailureException.class)
    @AroundValidSeatOnRedis // 동시성 Aspect 시그니처 어노테이션
    public void setSeatOwner(SelectSeatRequest selectSeatRequest) {
        // 빈자리일 경우만 DB 데이터 등록
        addSeatOwnerOnSql(selectSeatRequest);
    }

    public void addSeatOwnerOnSql(SelectSeatRequest request) throws ValidationFailureException {
        UpdateSeatUserRequest updateRequest = new UpdateSeatUserRequest(request.getSeatId(), request.getUserId());
        seatService.setUserOnEmptySeat(updateRequest);

        log.info("💽 SQL 저장 - '{}'님이 {} 자리 선택", request.getUserId(), request.getSeatId());
    }

    public void checkSeatOwnerOnRedis(SelectSeatRequest request) throws ValidationFailureException {
        String seatRedisKey = "seat:" + request.getClassroomId() + ":" + request.getSeatId();

        // Redis에 첫 번째 값이 맞는지 검증
        Set<Object> userSet = redisTemplate.opsForZSet().range(seatRedisKey, 0, 0);
        List<Object> userList = userSet.stream().toList();
        if (!userList.isEmpty() && request.getUserId() != Integer.parseInt((String) userList.get(0))) {
            throw new ValidationFailureException("NOT_AVAILABLE_SEAT");
        }

        log.info("✅ REDIS 검증 성공 - '{}'님이 {} 자리 선택", request.getUserId(), request.getSeatId());
    }

    public void addSeatUserOnRedis(SelectSeatRequest request) throws ValidationFailureException {
        String seatRedisKey = "seat:" + request.getClassroomId() + ":" + request.getSeatId();

        final long now = System.currentTimeMillis();
        int intNow = (int) (now % DIV_VALUE);
        redisTemplate.opsForZSet().add(seatRedisKey, String.valueOf(request.getUserId()), intNow);

        log.info("💥 REDIS 히스토리에 추가 - '{}'님이 {} 자리 선택", request.getUserId(), request.getSeatId());
    }

    public void completeSeatOnRedis(SelectSeatRequest request) {
        String seatRedisKey = "seat:" + request.getClassroomId() + ":" + request.getSeatId();

        // 시간 계산하기 위해 저장
        long now = System.currentTimeMillis();
        long timeHeader = (now / DIV_VALUE) * (long) DIV_VALUE;

        if (redisTemplate.hasKey(seatRedisKey)) {
            redisTemplate.rename(seatRedisKey, "selected:" + seatRedisKey + ":" + timeHeader);
        }
    }
}






