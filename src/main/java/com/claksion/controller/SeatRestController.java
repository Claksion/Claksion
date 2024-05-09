package com.claksion.controller;

import com.claksion.app.data.dto.SeatUser;
import com.claksion.app.data.dto.request.SelectSeatRequest;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.service.SeatSelectService;
import com.claksion.app.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/seat")
public class SeatRestController {

    final SeatService seatService;
    final SeatSelectService seatSelectService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/reset")
    public boolean resetSeat(@RequestParam(name = "classroomId") int classroomId) throws Exception {
        // REDIS reset
        Set<String> keys = redisTemplate.keys("seat:" + classroomId + ":*");
        for (String key : keys) {
            redisTemplate.delete(key);
        }
        redisTemplate.delete("completedSeat:" + classroomId);

        // SQL reset
        seatService.deleteByClassroomId(classroomId);

        String[] zoneArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        int numberMax = 4;

        for (String zone : zoneArray) {
            for (int number = 1; number <= numberMax; number++) {
                seatService.add(
                        SeatEntity.builder()
                                .classroomId(classroomId)
                                .zone(zone)
                                .number(number)
                                .build()
                );
            }
        }

        return true;
    }

    @PostMapping("/select")
    public boolean selectSeat(@RequestParam(name = "seatId") int seatId,
                              @RequestParam(name = "classroomId") int classroomId,
                              @RequestParam(name = "userId") int userId
    ) {
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

    @GetMapping("/result/detail")
    public List<SeatUser> resultDetail(@RequestParam(name = "seatId") int seatId, @RequestParam(name = "classroomId") int classroomId) throws Exception {
        List<SeatUser> redisUserList = seatSelectService.getRedisUserList(classroomId, seatId);
        log.info(redisUserList.toString());

        return redisUserList;
    }
}