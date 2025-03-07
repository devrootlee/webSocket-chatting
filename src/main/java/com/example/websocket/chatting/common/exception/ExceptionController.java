package com.example.websocket.chatting.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class ExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);
    // 그 외 오류
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity Exception(Exception e){
        LinkedHashMap body = new LinkedHashMap();

        body.put("resultCode",ValidationCode.SERVER_ERROR.getCode());

        LOGGER.error(e.getMessage(), e);

        return ResponseEntity.status(ValidationCode.SERVER_ERROR.getCode()).body(body);
    }
}
