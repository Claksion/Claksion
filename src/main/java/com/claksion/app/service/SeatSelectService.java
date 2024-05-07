package com.claksion.app.service;

import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatSelectService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SeatService seatService;

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
        int seatId = Integer.parseInt(seatKey.split(":")[2]);

        UpdateSeatUserRequest request = new UpdateSeatUserRequest(seatId, Integer.parseInt((String) userId));
        seatService.modifyUserId(request);

        log.info("✅'{}'님이 {} 자리 선택에 성공했습니다!", userId, seatKey);
        redisTemplate.delete(seatKey);
        redisTemplate.opsForSet().add("completedSeat:" + seatKey.split(":")[1], seatKey);
    }
}






