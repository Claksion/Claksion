package com.claksion.seat;

import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import com.claksion.app.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SeatServiceTests {

    @Autowired
    SeatService seatService;

    @DisplayName("Seat Service - modifyUserId")
    @Test
    public void testString() throws Exception {
        UpdateSeatUserRequest request = new UpdateSeatUserRequest(1475, 2);
        seatService.modifyUserId(request);
    }

    @DisplayName("Seat Service - existSeatByUserId")
    @Test
    public void testString2() throws Exception {
        boolean result = seatService.existSeatByUserId(1);
        log.info("======================");
        log.info("user 1 > "+result);
        log.info("======================");

        boolean result2 = seatService.existSeatByUserId(10);
        log.info("======================");
        log.info("user 10 > "+result2);
        log.info("======================");

    }

}
