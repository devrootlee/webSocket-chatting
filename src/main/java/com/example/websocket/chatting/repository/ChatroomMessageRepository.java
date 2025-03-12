package com.example.websocket.chatting.repository;

import com.example.websocket.chatting.model.ChatroomMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatroomMessageRepository extends MongoRepository<ChatroomMessage, String> {
}
