package com.claksion.seat;

import com.claksion.app.data.dto.request.SelectSeatRequest;
import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import com.claksion.app.repository.SeatRepository;
import com.claksion.app.service.SeatSelectService;
import com.claksion.app.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@SpringBootTest
class HyerimTests {
    @Autowired
    SeatSelectService seatSelectService;

    @Autowired
    SeatService seatService;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    // seat:1:A_1 - 1 (userId)

    public static int CLASSROOM_ID = 1;

    @Test
    void DB에_자리_주인_있니() {
        int seatId = 4197;
        int userId = 4;
        boolean existUserId = seatRepository.existUserId(seatId);
        log.info(seatId+">>>"+existUserId);
    }

    @Test
    void DB에_자리_주인_있으면_주인으로_못넣어() {
        int seatId = 4197;
        int userId = 4;
        Assertions.assertThrows(ValidationFailureException.class, () -> {
            seatService.setUserOnEmptySeat(new UpdateSeatUserRequest(seatId, userId));
        });
    }

    @Test
    void redis_키_넣는다() {
        redis_set_value();
    }

    @Test
    @Transactional
    void redis_transactional_적용된다() throws Exception {
        redis_set_value_with_exception();
    }

    void redis_set_value(){
        redisTemplate.opsForValue().set("test_redis_transactional","123");
    }

    void redis_set_value_with_exception() throws Exception{
        redis_set_value();
        throw new Exception();
    }

    @Test
    void around_annotation_적용된다(){
        try{
            SelectSeatRequest selectRequest = new SelectSeatRequest(4260,1,2);
            seatSelectService.addSeatUserOnRedis(selectRequest);
            seatSelectService.setSeatOwner(selectRequest);
            seatSelectService.completeSeatOnRedis(selectRequest);
            log.info("=============> "+true);
        }catch (ValidationFailureException e) {
            log.info("=============> "+false);
        }

    }

    @Test
    void 뒷자리_6개면_13분정도_차이남(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        Date date;
        long now;

        now = 1715189000000L;
        date = new Date();
        date.setTime(now);
        log.info(now+" >> "+simpleDateFormat.format(date));

        now = 1715189999999L;
        date = new Date();
        date.setTime(now);
        log.info(now+" >> "+simpleDateFormat.format(date));
    }

    @Test
    void 시간이_잘리면_얼마를_더해야할까(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        Date date;
        long now;

        int DIV_VALUE = 1000000;

        now = System.currentTimeMillis();
        date = new Date();
        date.setTime(now);
        log.info(now+" >> "+simpleDateFormat.format(date));

        int intNow = (int) (now % DIV_VALUE);

        log.info("intNow : "+intNow);

        long now2 = System.currentTimeMillis();
        long calcNow =intNow+(now2 / DIV_VALUE)*(long)DIV_VALUE;
        log.info("calcNow : "+calcNow);


        date = new Date();
        date.setTime(now);
        log.info(intNow+" >> "+simpleDateFormat.format(calcNow));
    }

    @Test
    void 동시에_100명이_달려든다면() throws InterruptedException {
        final int people = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(people);

        Random random = new Random();

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddWorker(countDownLatch, 4294,1, random.nextInt(1000)+1)))
                .limit(people)
                .collect(Collectors.toList());
        workers.forEach(Thread::start);
        countDownLatch.await();
    }

    private class AddWorker implements Runnable{
        private CountDownLatch countDownLatch;
        private int seatId;
        private int classroomId;
        private int userId;

        public AddWorker(CountDownLatch countDownLatch, int seatId, int classroomId, int userId) {
            this.countDownLatch = countDownLatch;
            this.seatId = seatId;
            this.classroomId = classroomId;
            this.userId = userId;
        }

        @Override
        public void run() {
            selectSeat(this.seatId, this.classroomId, this.userId);
            countDownLatch.countDown();
        }

        private boolean selectSeat(int seatId, int classroomId, int userId){
            try{
                SelectSeatRequest selectRequest = new SelectSeatRequest(seatId,classroomId,userId);
                seatSelectService.addSeatUserOnRedis(selectRequest);
                seatSelectService.setSeatOwner(selectRequest);
                seatSelectService.completeSeatOnRedis(selectRequest);
                log.info("=============> "+true);
                return true;
            }catch (ValidationFailureException e) {
                log.info("=============> "+false);
                return false;
            }
        }

    }



}
