package com.claksion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @RequestMapping("/")
    public String selectSeat() {
        // TODO : Redis 자리 선택 로직 작성
        return null;
    }


}
