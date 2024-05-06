package com.claksion.controller;

import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.data.entity.SeatEntity;
import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.service.ClassroomService;
import com.claksion.app.service.SeatService;
import com.claksion.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/seat")
public class SeatController {

    final UserService userService;
    final ClassroomService classroomService;
    final SeatService seatService;

    @RequestMapping("")
    public String getSeat(Model model, HttpSession session) throws Exception {
        UserEntity user = userService.get((Integer) session.getAttribute("userId"));
        ClassroomEntity classroom = classroomService.get(user.getClassroomId());
        List<SeatEntity> seats = seatService.getByClassroomId(user.getClassroomId());
        Map<String, List<SeatEntity>> seatMap = seats.stream().collect(Collectors.groupingBy(SeatEntity::getZone, HashMap::new, Collectors.toList()));

        List<List<SeatEntity>> seatList = new ArrayList<>(seats.stream().collect(Collectors.groupingBy(SeatEntity::getZone)).values());

        model.addAttribute("user", user);
        model.addAttribute("classroom", classroom);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("seatList", seatList);

        model.addAttribute("center", "seat");
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
