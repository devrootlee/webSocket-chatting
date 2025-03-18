package com.example.websocket.chatting.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chatroom")
public class Chatroom {
    @Id
    private String id; //MongoDB 고유 ID
    private String initiator; //생성자
    private String participant; //참여자
    private String lastMessage; //마지막 메시지
}
