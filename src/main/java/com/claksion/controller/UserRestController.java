package com.claksion.controller;

import com.claksion.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserRestController {

    final private UserService userService;

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String register(Model model) {
//        return "register";
//    }
}
