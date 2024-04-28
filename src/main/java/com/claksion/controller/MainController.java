package com.claksion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@Controller
public class MainController {
    @RequestMapping("/")
    public String main(Model model){
        return "index";
    }

    @RequestMapping("/testhyerm")
    public String testhyerm(Model model){
        return "seat_analysis";
    }
}
