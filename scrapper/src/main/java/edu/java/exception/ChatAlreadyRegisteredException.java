package edu.java.exception;

public class ChatAlreadyRegisteredException extends RuntimeException {

    public ChatAlreadyRegisteredException(long chatId) {
        super(String.format("Chat id = %d is already registered", chatId));
    }
}
