package com.example.websocket.chatting.controller;

import com.example.websocket.chatting.common.util.CommonUtil;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.model.ChatroomMessage;
import com.example.websocket.chatting.service.ChatService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ChatServiceController {

    CommonUtil commonUtil;
    ChatService chatService;

    //메시지 저장은 서비스단보단 컨트롤러에서 바로 처리
    private final SimpMessagingTemplate messagingTemplate;
    //채팅방 사용자 수를 저장하는 맵
    private final ConcurrentHashMap<String, Integer> roomUserCount = new ConcurrentHashMap<>();

    public ChatServiceController(CommonUtil commonUtil, ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
        this.commonUtil = commonUtil;
        this.chatService = chatService;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<Map<String, Object>> checkNickName(@RequestParam String nickname) {
        return commonUtil.ApiResponse(chatService.userNickNameCheck(nickname));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody ChatServiceRequestDto.register requestDto) {
        return commonUtil.ApiResponse(chatService.register(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody ChatServiceRequestDto.login requestDto, HttpServletResponse response) {
        Map<String, Object> serviceResponse = chatService.login(requestDto);
        System.out.println(serviceResponse);

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
    public ResponseEntity<Map<String, Object>> loginStatus(@AuthenticationPrincipal String nickname, HttpServletResponse response) {//@AuthenticationPrincipal 쿠키값에 있는 jwt 값을 이용하여 저장된 nickname 가져오기
        //인증된 사용자가 없으면 false
        if (nickname.equals("anonymousUser")) {
            // 쿠키 삭제
            Cookie cookie = new Cookie("jwt", null);
            cookie.setAttribute("JSESSIONID", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/"); // 쿠키의 경로 설정
            cookie.setMaxAge(0); // 쿠키의 만료 시간을 0으로 설정하여 삭제
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("loginStatus", false));
        }

        return ResponseEntity.ok(Map.of("loginStatus", true, "nickname", nickname));
    }

    @GetMapping("/memberList")
    public ResponseEntity<Map<String, Object>> memberList(@AuthenticationPrincipal String nickname) {
        return commonUtil.ApiResponse(chatService.memberList(nickname));
    }

    @PostMapping("/chatroom")
    public ResponseEntity<Map<String, Object>> chatroomCreate(@AuthenticationPrincipal String nickname, @RequestBody ChatServiceRequestDto.chatroomCreate requestDto) {
        return commonUtil.ApiResponse(chatService.chatroomCreate(nickname, requestDto));
    }

    @GetMapping("/chatroomList")
    public ResponseEntity<Map<String, Object>> chatroomList(@AuthenticationPrincipal String nickname) {
        return commonUtil.ApiResponse(chatService.chatroomList(nickname));
    }

    @GetMapping("/chatroomInfo")
    public ResponseEntity<Map<String, Object>> chatroomInfo(@RequestParam String roomId) {
        return commonUtil.ApiResponse(chatService.chatroomInfo(roomId));
    }

    @DeleteMapping("/chatroom")
    public void chatroomDelete(@RequestParam String roomId) {
        chatService.chatroomDelete(roomId);
    }

    @MessageMapping("/chatroom/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, ChatroomMessage message) {
        message.setTimestamp(LocalDateTime.now());
        messagingTemplate.convertAndSend("/chatroom/" + roomId, message);

        chatService.chatroomMessageSave(roomId, message);
    }
}
