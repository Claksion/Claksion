package com.claksion.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat")
public class SeatRestController {

    @RequestMapping("/set")
    public String setSeat() {
        return null;
    }


}
