package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        System.out.println(message.getSender());
        System.out.println(message.getContent());
        System.out.println(message.getType());
        return message;
    }
}
