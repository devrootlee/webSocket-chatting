package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.model.User;
import com.example.websocket.chatting.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ChatServiceController {

    UserService userService;

    public ChatServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/checkUserId")
    public Map<String,Boolean> checkUserId(@RequestParam String userId) {
        Map<String,Boolean> result = new HashMap<>();
        result.put("available", true);

        return result;
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute User user) {
        return userService.signUp(user).getUserId();
    }
}
