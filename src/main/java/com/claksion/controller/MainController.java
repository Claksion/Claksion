package com.claksion.controller;

import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping("/")
    public String main(Model model, HttpSession session) throws Exception {
        if(httpSession == null || session.getAttribute("userId") == null)
            return "redirect:/user/login";

        int userId = (int) session.getAttribute("userId");
        UserEntity userEntity = userService.get(userId);
        model.addAttribute("user", userEntity);
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
}
