package com.example.websocket.chatting.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chat_room")
public class ChatRoom {
    @Id
    private String id; // _id 값을 받는 필드
    private String name;
    private String description;
    private int maxParticipants;
    private String nickName;
}
