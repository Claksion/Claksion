package com.claksion.app.service;

import com.claksion.app.data.dto.SeatSelectCount;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.frame.BaseService;
import com.claksion.app.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatSelectService {
    private final RedisTemplate<String,Object> redisTemplate;
    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private final SeatService seatService;
    private SeatSelectCount seatSelectCount;

    public void setEventCount(String seatId, int queue){
        this.seatSelectCount = new SeatSelectCount(seatId, queue);
    }

    public void addQueue(String seatId) {
        final String people = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(seatId, people, (int) now);
        log.info("대기열에 추가 - {} ({}초)", people, now);
    }

    public void getOrder(String seatId){
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<Object> queue = redisTemplate.opsForZSet().range(seatId, start, end);

        for (Object people : queue) {
            Long rank = redisTemplate.opsForZSet().rank(seatId, people);
            log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, rank);
        }
    }

    public void publish(String seatId){
        Set<Object> queue = redisTemplate.opsForZSet().range(seatId, 0, 0);
        log.info("******* "+queue);
        for (Object people : queue) {
            log.info("'{}'님이 {} 자리 선택에 성공했습니다!",people, seatId);
            redisTemplate.opsForZSet().remove(seatId, people);
            this.seatSelectCount.decrease();
        }
    }

    public boolean validEnd(){
        // TODO : DB에 값 지정되어 있는지?
        return this.seatSelectCount != null
                ? this.seatSelectCount.end()
                : false;
    }

    public long getSize(String seatId){
        return redisTemplate.opsForZSet().size(seatId);
    }
}
