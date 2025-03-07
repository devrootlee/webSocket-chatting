package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.common.util.CommonUtil;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChatServiceController {

    CommonUtil commonUtil;
    ChatService chatService;

    public ChatServiceController(CommonUtil commonUtil, ChatService chatService) {
        this.commonUtil =commonUtil;
        this.chatService = chatService;
    }

    @GetMapping("/checkNickName")
    public ResponseEntity<Map<String, Object>> checkNickName(@RequestParam String nickName) {
        return commonUtil.ApiResponse(chatService.userNickNameCheck(nickName));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody ChatServiceRequestDto.register requestDto) {
        return commonUtil.ApiResponse(chatService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody ChatServiceRequestDto.login requestDto) {
        return commonUtil.ApiResponse(chatService.login(requestDto));
    }
}
