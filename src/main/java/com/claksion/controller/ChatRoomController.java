package com.claksion.controller;


// import 생략...

import com.claksion.app.data.dto.msg.ChatRoom;
import com.claksion.app.service.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
@Slf4j
public class ChatRoomController {
    @Value("${app.url.server-url}")
    String severurl;

    private final ChatRoomRepository chatRoomRepository;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        model.addAttribute("center","chat/room");
        return "index";
    }
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        List<ChatRoom> rooms = chatRoomRepository.findAllRoom();
        log.info(rooms.toString());
        return rooms;
    }
    // 채팅방 생성
    @PostMapping("/room2")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam("name") String name) {
        return chatRoomRepository.createChatRoom(name);
    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter")
    public String roomDetail(Model model, @RequestParam("roomId") String roomId) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("serverurl",severurl);
        model.addAttribute("center","chat/roomdetail");
        return "index";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}