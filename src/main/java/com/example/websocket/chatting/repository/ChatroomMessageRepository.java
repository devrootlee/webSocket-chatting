package com.example.websocket.chatting.repository;

import com.example.websocket.chatting.model.ChatroomMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatroomMessageRepository extends MongoRepository<ChatroomMessage, String> {
    List<ChatroomMessage> findByRoomId(String roomId);
}
