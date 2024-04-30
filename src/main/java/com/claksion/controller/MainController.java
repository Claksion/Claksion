package com.claksion.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Controller
public class MainController {
    @Value("${app.url.server-url}")
    String severurl;

    @RequestMapping("/")
    public String main(Model model){
        return "index";
    }
    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
    @RequestMapping("/testhayoung")
    public String testhayoung(Model model){
        return "seat_select";
    }

    @RequestMapping("/testhyerm")
    public String testhyerm(Model model){
        return "seat_analysis";
    }
    @RequestMapping("/chat")
    public String chat(Model model, HttpSession httpSession){
        httpSession.setAttribute("id","hong");
        model.addAttribute("serverurl",severurl);
        return "chat";
    }
}
