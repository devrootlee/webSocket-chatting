package com.example.websocket.chatting.repository;

import com.example.websocket.chatting.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
