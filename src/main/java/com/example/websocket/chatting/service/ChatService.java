package com.example.websocket.chatting.service;

import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.model.Member;

import java.util.Map;

public interface ChatService {
    //닉네임 중복 확인
    Map userNickNameCheck(String nickName);

    //회원가입
    Map register(ChatServiceRequestDto.register requestDto);

    //로그인
    Map login(ChatServiceRequestDto.login requestDto);
}
