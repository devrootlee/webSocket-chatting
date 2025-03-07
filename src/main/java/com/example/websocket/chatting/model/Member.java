package com.example.websocket.chatting.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "member")
public class Member {
    @Indexed(unique = true)
    private String nickName;

    private String password;
}
