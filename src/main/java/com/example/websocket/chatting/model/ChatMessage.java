package com.example.websocket.chatting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

        private String sender; //발신자

        private String content; //내용

        private MessageType type;

        public enum MessageType {
            CHAT, JOIN, LEAVE
        }

}
