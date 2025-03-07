package com.example.websocket.chatting.common.exception;

public enum ValidationCode {
    SUCCESS(200),
    REQUEST_ERROR(400),
    SERVER_ERROR(500);

    private final int code;

    ValidationCode(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }
}
