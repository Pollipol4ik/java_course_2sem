package edu.java.exception;

import org.springframework.http.HttpStatus;

public class ChatAlreadyRegisteredException extends BaseException {

    public ChatAlreadyRegisteredException(long chatId) {
        super(String.format("Chat id = %d is already registered", chatId), HttpStatus.CONFLICT);
    }
}
