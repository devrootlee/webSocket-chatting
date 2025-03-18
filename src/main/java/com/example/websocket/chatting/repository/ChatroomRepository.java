package com.example.websocket.chatting.repository;

import com.example.websocket.chatting.model.Chatroom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends MongoRepository<Chatroom, String> {
    Optional<Chatroom> findById(String id);

    Chatroom findByInitiatorAndParticipant(String initiator, String participant);

    List<Chatroom> findByInitiatorOrParticipant(String initiator, String participant);
}
