package com.claksion.controller;

import com.claksion.app.service.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedisController {
    private RedisService service = new RedisService();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        if (userId != null) {
            service.reserveSeat(userId);
        }
        request.getRequestDispatcher("/views/reservation.jsp").forward(request, response);
    }
}

