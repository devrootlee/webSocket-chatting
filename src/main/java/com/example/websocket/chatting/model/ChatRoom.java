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
    private String id;
    private String websocketId;
    private String name;
    private String nickName;
}
