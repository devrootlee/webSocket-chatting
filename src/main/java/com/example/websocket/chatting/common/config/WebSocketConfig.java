package com.example.websocket.chatting.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

   @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       //chatroom으로 시작하는 메시지를 구독하는 클라이언트에게 전달
       registry.enableSimpleBroker("/chatroom");
       registry.setApplicationDestinationPrefixes("/app");
   }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 접속할 WebSocket 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}