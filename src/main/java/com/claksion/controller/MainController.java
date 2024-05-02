package com.claksion.controller;

import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.service.ClassroomService;
import com.claksion.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    final private UserService userService;
    private final HttpSession httpSession;
    private final ClassroomService classroomService;
  
    @Value("${app.url.serverurl}")
    String severurl;

    @RequestMapping("/")
    public String main(Model model, HttpSession session) throws Exception {
        if(httpSession == null || session.getAttribute("userId") == null)
            return "redirect:/user/login";

        int userId = (int) session.getAttribute("userId");
        UserEntity user = userService.get(userId);
        model.addAttribute("user", user);

        ClassroomEntity classroom = classroomService.get(user.getClassroomId());
        model.addAttribute("classroom", classroom);

        List<UserEntity> classroomMates = userService.getByClassroomId(user.getClassroomId());
        model.addAttribute("classroomMates", classroomMates);

        return "index";
    }

    @RequestMapping("/testhayoung")
    public String testhayoung(Model model) {
        return "seat_select";
    }

    @RequestMapping("/testhyerm")
    public String testhyerm(Model model) {
        return "seat_analysis";
    }
    @RequestMapping("/chat")
    public String chat(Model model, HttpSession httpSession){
        httpSession.setAttribute("id","hong");
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chat");
        return "index";
    }
    @RequestMapping("/chat2")
    public String chat2(Model model, HttpSession httpSession){
        httpSession.setAttribute("id","hong");
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chat2");
        return "chat2";
    }
}
