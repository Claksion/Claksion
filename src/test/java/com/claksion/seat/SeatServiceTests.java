package com.claksion.seat;

import com.claksion.app.data.dto.request.UpdateSeatUserRequest;
import com.claksion.app.data.dto.response.GetSeatAndUserResponse;
import com.claksion.app.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class SeatServiceTests {

    @Autowired
    SeatService seatService;

    @DisplayName("Seat Service - modifyUserId")
    @Test
    public void testString() throws Exception {
        UpdateSeatUserRequest request = new UpdateSeatUserRequest(1475, 2);
        seatService.setUserOnEmptySeat(request);
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

    @DisplayName("Seat Service - getSeatAndUserResponse")
    @Test
    public void testString3() throws Exception {
        List<GetSeatAndUserResponse> list = seatService.getSeatAndUserByClassroomId(1);
        for(GetSeatAndUserResponse getSeatAndUserResponse : list) {
            log.info(">"+getSeatAndUserResponse.toString());
        }
    }

}
