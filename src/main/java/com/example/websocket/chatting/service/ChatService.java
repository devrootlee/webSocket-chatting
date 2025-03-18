package com.example.websocket.chatting.service;

import com.example.websocket.chatting.common.security.JwtProvider;
import com.example.websocket.chatting.dto.ChatServiceRequestDto;
import com.example.websocket.chatting.dto.MemberListDto;
import com.example.websocket.chatting.model.ChatroomMessage;
import com.example.websocket.chatting.model.Chatroom;
import com.example.websocket.chatting.model.Member;
import com.example.websocket.chatting.repository.ChatroomMessageRepository;
import com.example.websocket.chatting.repository.ChatroomRepository;
import com.example.websocket.chatting.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    MemberRepository memberRepository;
    ChatroomRepository chatroomRepository;
    ChatroomMessageRepository chatroomMessageRepository;

    public ChatService(
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

    //멤버 리스트
    public Map<String, Object> memberList(String nickname) {
        Map<String, Object> result = new HashMap<>();

        List<Member> memberList = memberRepository.findByNicknameNot(nickname);//자기 자신 제외
        List<MemberListDto> memberResponseList = memberList.stream()
                .map(MemberListDto::new) // Member를 DTO로 매핑
                .collect(Collectors.toList());

        result.put("memberList", memberResponseList);

        return result;
    }

    //채팅방 생성
    public Map<String, Object> chatroomCreate(String nickname, ChatServiceRequestDto.chatroomCreate requestDto) {
        Map<String, Object> result = new HashMap<>();

        //기존 채팅방이 있으면 생성안함
        Chatroom existingChatroom1 = chatroomRepository.findByInitiatorNicknameAndParticipantNickname(nickname, requestDto.getParticipantNickname());
        if (existingChatroom1 != null) {
            result.put("chatroom", existingChatroom1);
            return result;
        }

        //반대 경우
        Chatroom existingChatroom2 = chatroomRepository.findByInitiatorNicknameAndParticipantNickname(requestDto.getParticipantNickname(), nickname);
        if (existingChatroom2 != null) {
            result.put("chatroom", existingChatroom2);
            return result;
        }

        Chatroom chatroom = Chatroom.builder()
                .initiatorNickname(nickname)
                .participantNickname(requestDto.getParticipantNickname())
                .build();

        chatroomRepository.save(chatroom);

        result.put("chatroom", chatroom);
        return result;
    }

    public Map<String, Object> chatroomList() {
        Map<String, Object> result = new HashMap<>();

        List<Chatroom> chatroomList = chatroomRepository.findAll();
        result.put("chatroomList", chatroomList);

        return result;
    }

    public Map<String, Object> chatroomInfo(String roomId) {
        Map<String, Object> result = new HashMap<>();
        Optional<Chatroom> chatRoom = chatroomRepository.findById(roomId);
        List<ChatroomMessage> chatroomMsgList = chatroomMessageRepository.findByRoomId(roomId);

        result.put("chatroomMsgList", chatroomMsgList);
        result.put("chatroom", chatRoom);

        return result;
    }

    public void chatroomDelete(String roomId) {
        Map<String, Object> result = new HashMap<>();
        chatroomRepository.findById(roomId).ifPresent(chatroomRepository::delete);
    }

    public void chatroomMessageSave(String roomId, ChatroomMessage message) {
        message.setRoomId(roomId);
        chatroomMessageRepository.save(message);
    }
}
