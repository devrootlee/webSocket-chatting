package com.example.websocket.chatting.service;

import com.example.websocket.chatting.model.User;
import com.example.websocket.chatting.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //ID 중복 체크
    public void userIdCheck(String userId) {

    }

    //회원 가입
    public User signUp(User user) {

        userRepository.save(user);

        return user;
    }

    //로그인
    public User login(User user) {

        return user;
    }
}
