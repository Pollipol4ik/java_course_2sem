package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends BaseException {

    public ChatNotFoundException(long chatId) {
        super(String.format("Chat id = %d not found", chatId), HttpStatus.NOT_FOUND);
    }
}
