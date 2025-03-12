package com.example.websocket.chatting.service;

import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.model.ChatroomMessage;

import java.util.Map;

public interface ChatService {
    //닉네임 중복 확인
    Map<String, Object> userNickNameCheck(String nickname);

    //회원가입
    Map<String, Object> register(ChatServiceRequestDto.register requestDto);

    //로그인
    Map<String, Object> login(ChatServiceRequestDto.login requestDto);

    //채팅방 생성
    Map<String, Object> chatroomCreate(String nickname, ChatServiceRequestDto.chatroomCreate requestDto);

    //채팅방 전체 조회
    Map<String, Object> chatroomList();

    //채팅방 조회
    Map<String, Object> chatroom(String roomId);

    Map<String, Object> chatroomMessageSave(String roomId, ChatroomMessage chatroomMessage);
}
