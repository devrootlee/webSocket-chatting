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
        private String id; //MongoDB 고유 ID
        private String roomId; //채팅방 ID
        private String sender; //발신자
        private String message; //메시지
        private LocalDateTime timestamp; //발송시간
}
