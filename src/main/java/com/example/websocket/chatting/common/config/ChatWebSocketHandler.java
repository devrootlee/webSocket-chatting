package com.example.websocket.chatting.common.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId == null) {
            session.close(CloseStatus.BAD_DATA.withReason("Invalid roomId"));
            System.out.println("Invalid roomId, closing session: " + session.getId());
            return;
        }

        roomSessions.put(roomId, session);
        session.sendMessage(new TextMessage("Connected to room " + roomId));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId == null) return;

        WebSocketSession currentSession = roomSessions.get(roomId);
        if (currentSession != null && currentSession.isOpen()) {
            currentSession.sendMessage(new TextMessage("Echo: " + message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomId(session);
        if (roomId != null) {
            roomSessions.remove(roomId);
        }
    }

    private String extractRoomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        String[] parts = uri.split("/");
        return parts.length > 2 ? parts[parts.length - 1] : null;
    }
}
