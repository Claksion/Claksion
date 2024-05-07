package com.claksion.controller;

import com.claksion.app.data.dto.ClassMate;
import com.claksion.app.data.entity.ClassroomEntity;
import com.claksion.app.data.entity.UserEntity;
import com.claksion.app.service.ClassroomService;
import com.claksion.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    final private UserService userService;
    private final HttpSession httpSession;
    private final ClassroomService classroomService;
  
    @Value("${app.url.server-url}")
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

        List<ClassMate> classMates = userService.getClassMates(user.getClassroomId());
        model.addAttribute("classMates", classMates);

        return "index";
    }

    @RequestMapping("/chat")
    public String chat(Model model, HttpSession httpSession){
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chat");
        return "index";
    }
    @RequestMapping("/chatroom")
    public String chatroom(Model model, HttpSession httpSession){
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chatroom");
        return "index";
    }
    @RequestMapping("/chatroomdetail")
    public String chatroomdetail(Model model, HttpSession httpSession){
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chatroomdetail");
        return "index";
    }
}
