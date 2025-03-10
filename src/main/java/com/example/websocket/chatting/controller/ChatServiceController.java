package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.common.util.CommonUtil;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.service.ChatService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChatServiceController {

    CommonUtil commonUtil;
    ChatService chatService;

    public ChatServiceController(CommonUtil commonUtil, ChatService chatService) {
        this.commonUtil = commonUtil;
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody ChatServiceRequestDto.login requestDto, HttpServletResponse response, HttpSession session) {
        Map<String, Object> serviceResponse = chatService.login(requestDto);

        if (serviceResponse.get("jwt") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "JWT 생성 실패"));
        }

        String jwt = serviceResponse.get("jwt").toString();
        Cookie cookie = new Cookie("jwt", jwt); //jwt
        //클라이언트에서 접근할 수없도록 설정
        cookie.setHttpOnly(true);
        //전체 애플리케이션 경로에 대해 유효
        cookie.setPath("/");
        //1시간 동안 유효
        cookie.setMaxAge(60 * 60);
        //Https 환경에서만 전송하도록 설정
        //cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/loginStatus")
    public ResponseEntity<Map<String, Object>> loginStatus(@AuthenticationPrincipal String nickName) {//@AuthenticationPrincipal 쿠키값에 있는 jwt 값을 이용하여 저장된 nickName 가져오기
        //인증된 사용자가 없으면 false
        if (nickName.equals("anonymousUser")) {
            return ResponseEntity.ok(Map.of("login", false));
        }

        return ResponseEntity.ok(Map.of("login", true, "nickName", nickName));
    }

    @PostMapping("/chatroom/create")
    public ResponseEntity<Map<String, Object>> chatCreate(@AuthenticationPrincipal String nickName, @RequestBody ChatServiceRequestDto.chatroomCreate requestDto) {
        return commonUtil.ApiResponse(chatService.chatroomCreate(nickName, requestDto));
    }

    @GetMapping("/chatroom")
    public ResponseEntity<Map<String, Object>> chatroom() {
        return commonUtil.ApiResponse(chatService.chatroom());
    }
}
