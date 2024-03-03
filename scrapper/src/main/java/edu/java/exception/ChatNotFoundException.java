package edu.java.exception;

public class ChatNotFoundException extends RuntimeException {

    public ChatNotFoundException(long chatId) {
        super("Чат id = %d не найден".formatted(chatId));
    }
}
