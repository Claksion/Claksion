package com.claksion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seat")
public class SeatController {

    @RequestMapping("")
    public String getSeat(Model model) {
        model.addAttribute("center","seat");
        return "index";
    }

    @RequestMapping("/testhyerm")
    public String testhyerm(Model model) {
        return "seat_analysis";
    }

    @PostMapping("/userinfo")
    public String getUserInfo(@RequestParam String userId) {
        // 실제 애플리케이션에서는 여기서 데이터베이스 조회 등의 로직을 추가할 수 있습니다.
        return "Received user ID: " + userId;
    }


}
