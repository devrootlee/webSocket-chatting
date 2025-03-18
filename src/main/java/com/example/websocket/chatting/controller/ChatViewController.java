package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.model.ChatroomMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatViewController {

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/chatroom/create")
    public String chatroomCreate() {
        return "chatroom-create";
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "chatroom";
    }

    @MessageMapping("/x-chat")
    @SendTo("/topic/messages")
    public ChatroomMessage sendMessage(ChatroomMessage message) {
        return message;
    }
}
