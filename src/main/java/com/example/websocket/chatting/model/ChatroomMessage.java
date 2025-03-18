package com.example.websocket.chatting.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "chatroom_message")
public class ChatroomMessage {
        @Id
        private String id;
        private String roomId;
        private String sender;
        private String message;
        private LocalDateTime timestamp;
}
