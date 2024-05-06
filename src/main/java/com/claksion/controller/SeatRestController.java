package com.claksion.controller;

import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/seat")
public class SeatRestController {

    final SeatService seatService;

    @PostMapping("/reset")
    public boolean resetSeat(@RequestParam(name = "classroomId") int classroomId) throws Exception {
        seatService.deleteByClassroomId(classroomId);

        String[] zoneArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
        int numberMax = 4;

        for(String zone: zoneArray) {
            for(int number = 1; number <= numberMax; number++) {
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
}
