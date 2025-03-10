package com.example.websocket.chatting.service.impl;

import com.example.websocket.chatting.common.security.JwtProvider;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.model.ChatRoom;
import com.example.websocket.chatting.model.Member;
import com.example.websocket.chatting.repository.ChatRoomRepository;
import com.example.websocket.chatting.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements com.example.websocket.chatting.service.ChatService {
    JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    MemberRepository memberRepository;
    ChatRoomRepository chatRoomRepository;

    public ChatServiceImpl(
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            MemberRepository memberRepository,
            ChatRoomRepository chatRoomRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    //닉네임 중복 체크
    @Override
    public Map<String, Object> userNickNameCheck(String nickName) {
        Map<String, Object> result = new HashMap<>();

        Member member = memberRepository.findByNickName(nickName);

        if (member != null) {
            result.put("available", false);
        } else {
            result.put("available", true);
        }

        return result;
    }

    //회원 가입
    @Override
    public Map<String, Object> register(ChatServiceRequestDto.register requestDto) {
        Map<String, Object> result = new HashMap<>();

        Member member = Member.builder()
                .nickName(requestDto.getNickName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        memberRepository.save(member);

        result.put("member", member);
        return result;
    }

    //로그인
    @Override
    public Map<String, Object> login(ChatServiceRequestDto.login requestDto) {
        Map<String, Object> result = new HashMap<>();
        Member member = memberRepository.findByNickName(requestDto.getNickName());
        //회원이 존재할 경우
        if (member != null) {
            //비밀번호 확인
            if (passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
                //jwt 생성
                String jwt = jwtProvider.generateJwt(member.getNickName());

                result.put("jwt", jwt);
            } else {
                result.put("jwt", null);
            }
        } else {
            result.put("jwt", null);
        }
        return result;
    }

    @Override
    public Map<String, Object> chatroomCreate(String nickName, ChatServiceRequestDto.chatroomCreate requestDto) {
        Map<String, Object> result = new HashMap<>();

        ChatRoom chatRoom = ChatRoom.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .maxParticipants(requestDto.getMaxParticipants())
                .nickName(nickName)
                .build();

        chatRoomRepository.save(chatRoom);
        result.put("chatRoom", chatRoom);
        return result;
    }

    @Override
    public Map<String, Object> chatroom() {
        Map<String, Object> result = new HashMap<>();

        List<ChatRoom> chatroomList = chatRoomRepository.findAll();
        result.put("chatroomList", chatroomList);

        return result;
    }
}
