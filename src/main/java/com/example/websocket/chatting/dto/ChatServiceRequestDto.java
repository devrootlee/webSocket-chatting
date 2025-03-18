package com.example.websocket.chatting.dto;

import lombok.*;

public class ChatServiceRequestDto {

    @Getter
    @Setter
    public static class register {
        private String nickname;

        private String password;
    }

    @Getter
    @Setter
    public static class login {
        private String nickname;

        private String password;
    }

    @Getter
    @Setter
    public static class chatroomCreate {
        private String participantNickname;
    }

    @Getter
    @Setter
    public static class chatroom {
        private String roomId;
    }
}
