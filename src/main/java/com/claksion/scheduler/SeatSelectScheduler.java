package com.claksion.scheduler;

import com.claksion.app.service.SeatSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatSelectScheduler {

    private final SeatSelectService seatSelectService;

    @Scheduled(fixedDelay = 1000)
    private void seatSelectScheduler(){
        if(seatSelectService.validEnd()){
            log.info("===== 선착순 이벤트가 종료되었습니다. =====");
            return;
        }
        seatSelectService.publish("A01");
        seatSelectService.getOrder("A01");
    }
}