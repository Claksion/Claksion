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

    @DisplayName("Data Type이 String인 경우 테스트")
    @Test
    public void testString() throws Exception {
        UpdateSeatUserRequest request = new UpdateSeatUserRequest(1475, 2);
        seatService.modifyUserId(request);
    }
}
