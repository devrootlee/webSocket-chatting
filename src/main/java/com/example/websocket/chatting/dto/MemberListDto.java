package com.example.websocket.chatting.dto;

import com.example.websocket.chatting.model.Member;
import lombok.Data;

@Data
public class MemberListDto {
    private String nickname;

    // 생성자
    public MemberListDto(Member member) {
        this.nickname = member.getNickname();
    }
}
