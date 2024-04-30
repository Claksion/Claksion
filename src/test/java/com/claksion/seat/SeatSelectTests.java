package com.claksion.seat;

import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.service.SeatSelectService;
import com.claksion.app.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@SpringBootTest
class SeatSelectTests {
    @Autowired
    SeatSelectService seatSelectService;

    @Autowired
    SeatService seatService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    // seat:1:A_1 - 1 (userId)

    public static int CLASSROOM_ID = 1;

    @Test
    void 담임이_자리배치도_생성() throws Exception {

        // 이미 생성해놨던 자리 삭제 (redis)
        Set<String> keys = redisTemplate.keys("seat:"+CLASSROOM_ID+":*");
        for(String key : keys) {
            redisTemplate.delete(key);
        }
        redisTemplate.delete("completedSeat:"+CLASSROOM_ID);

        // 이미 생성해놨던 자리 삭제 (sql)
        seatService.deleteByClassroomId(CLASSROOM_ID);

        // 자리 데이터 생성 (sql)
        SeatEntity seat1 = new SeatEntity(0, "A", 1, 0, CLASSROOM_ID);
        SeatEntity seat2 = new SeatEntity(0, "A", 2, 0, CLASSROOM_ID);
        SeatEntity seat3 = new SeatEntity(0, "A", 3, 0, CLASSROOM_ID);
        seatService.add(seat1);
        seatService.add(seat2);
        seatService.add(seat3);
    }

    @Test
    void 레디스_데이터_불러와서_자리_선택하기() throws InterruptedException {
        final int people = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(people);

        redisTemplate.delete("completedSeat:"+CLASSROOM_ID);

        // 실행 전 redis에서 다음 명령어 칠 것 >> FLUSHALL
//        redisTemplate.getConnectionFactory().getConnection().flushAll();

        String[] seatKeyList = new String[]{"seat:1:A_1","seat:1:A_2","seat:1:A_3"};
        Random random = new Random();

        List<Thread> workers = Stream
                .generate(() -> new Thread(new AddQueueWorker(countDownLatch, seatKeyList[random.nextInt(3)])))
                .limit(people)
                .collect(Collectors.toList());
        workers.forEach(Thread::start);
        countDownLatch.await();
        Thread.sleep(10000); // 기프티콘 발급 스케줄러 작업 시간
    }

    private class AddQueueWorker implements Runnable{
        private CountDownLatch countDownLatch;
        private String seatId;

        public AddQueueWorker(CountDownLatch countDownLatch, String seatId) {
            this.countDownLatch = countDownLatch;
            this.seatId = seatId;
        }

        @Override
        public void run() {
            seatSelectService.addQueue(seatId);
            countDownLatch.countDown();
        }
    }
}
