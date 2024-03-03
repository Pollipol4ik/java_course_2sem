package edu.java.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException(long chatId) {
        super(String.format("Chat id = %d not found", chatId));
    }
}
