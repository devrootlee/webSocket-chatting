package com.example.websocket.chatting.repository;

import com.example.websocket.chatting.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByNickname(String nickname);
}
