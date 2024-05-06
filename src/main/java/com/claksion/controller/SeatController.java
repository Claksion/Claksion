package com.claksion.controller;

import com.claksion.app.data.dto.response.GetSeatAndUserResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
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
        List<List<SeatEntity>> seatList = new ArrayList<>(seats.stream().collect(Collectors.groupingBy(SeatEntity::getZone)).values());

        boolean existSeat = seatService.existSeatByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("classroom", classroom);
        model.addAttribute("seatList", seatList);
        model.addAttribute("canSelect", !existSeat);

        model.addAttribute("center", "seat");
        model.addAttribute("detail", "seat_select");
        return "index";
    }

    @RequestMapping("/result")
    public String testhyerm(Model model, HttpSession session) throws Exception {
        UserEntity user = userService.get((Integer) session.getAttribute("userId"));

        ClassroomEntity classroom = classroomService.get(user.getClassroomId());
        List<GetSeatAndUserResponse> seats = seatService.getSeatAndUserByClassroomId(user.getClassroomId());
        List<List<GetSeatAndUserResponse>> seatList = new ArrayList<>(seats.stream().collect(Collectors.groupingBy(GetSeatAndUserResponse::getZone)).values());

        model.addAttribute("user", user);
        model.addAttribute("classroom", classroom);
        model.addAttribute("seatList", seatList);

        model.addAttribute("center", "seat");
        model.addAttribute("detail", "seat_result");
        return "index";
    }
}
