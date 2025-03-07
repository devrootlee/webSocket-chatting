package com.example.websocket.chatting.dto;

import lombok.*;

public class ChatServiceRequestDto {

    @Getter
    @Setter
    public static class register {
        private String nickName;

        private String password;
    }

    @Getter
    @Setter
    public static class login {
        private String nickName;

        private String password;
    }
}
