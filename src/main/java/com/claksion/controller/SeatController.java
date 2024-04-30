package com.claksion.controller;

import com.claksion.app.service.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @RequestMapping("/")
    public String selectSeat() {
        // TODO : Redis 자리 선택 로직 작성
        return null;
    }
    @PostMapping("/userinfo")
    public String getUserInfo(@RequestParam String userId) {
        // 실제 애플리케이션에서는 여기서 데이터베이스 조회 등의 로직을 추가할 수 있습니다.
        return "Received user ID: " + userId;
    }


}
