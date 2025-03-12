package com.example.websocket.chatting.service.impl;

import com.example.websocket.chatting.common.security.JwtProvider;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.model.ChatroomMessage;
import com.example.websocket.chatting.model.Chatroom;
import com.example.websocket.chatting.model.Member;
import com.example.websocket.chatting.repository.ChatroomMessageRepository;
import com.example.websocket.chatting.repository.ChatroomRepository;
import com.example.websocket.chatting.repository.MemberRepository;
import com.example.websocket.chatting.service.ChatService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    MemberRepository memberRepository;
    ChatroomRepository chatroomRepository;
    ChatroomMessageRepository chatroomMessageRepository;

    public ChatServiceImpl(
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            MemberRepository memberRepository,
            ChatroomRepository chatroomRepository,
            ChatroomMessageRepository chatroomMessageRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.chatroomRepository = chatroomRepository;
        this.chatroomMessageRepository = chatroomMessageRepository;
    }

    //닉네임 중복 체크
    @Override
    public Map<String, Object> userNickNameCheck(String nickname) {
        Map<String, Object> result = new HashMap<>();

        Member member = memberRepository.findByNickname(nickname);

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
                .nickname(requestDto.getNickname())
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
        Member member = memberRepository.findByNickname(requestDto.getNickname());
        //회원이 존재할 경우
        if (member != null) {
            //비밀번호 확인
            if (passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
                //jwt 생성
                String jwt = jwtProvider.generateJwt(member.getNickname());

                result.put("jwt", jwt);
                result.put("nickname", requestDto.getNickname());
            } else {
                result.put("jwt", null);
            }
        } else {
            result.put("jwt", null);
        }
        return result;
    }

    @Override
    public Map<String, Object> chatroomCreate(String nickname, ChatServiceRequestDto.chatroomCreate requestDto) {
        Map<String, Object> result = new HashMap<>();

        Chatroom chatRoom = Chatroom.builder()
                .name(requestDto.getName())
                .nickname(nickname)
                .build();

        chatroomRepository.save(chatRoom);


        result.put("chatroom", chatRoom);
        return result;
    }

    @Override
    public Map<String, Object> chatroomList() {
        Map<String, Object> result = new HashMap<>();

        List<Chatroom> chatroomList = chatroomRepository.findAll();
        result.put("chatroomList", chatroomList);

        return result;
    }

    @Override
    public Map<String, Object> chatroom(String roomId) {
        Map<String, Object> result = new HashMap<>();
        Optional<Chatroom> chatRoom = chatroomRepository.findById(roomId);

        result.put("chatroom", chatRoom);

        return result;
    }

    @Override
    public Map<String, Object> chatroomMessageSave(String roomId, ChatroomMessage message) {

        message.setRoomId(roomId);
        chatroomMessageRepository.save(message);

        return Map.of();
    }
}
