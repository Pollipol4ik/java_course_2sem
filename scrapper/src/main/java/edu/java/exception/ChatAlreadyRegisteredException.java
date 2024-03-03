package edu.java.exception;

public class ChatAlreadyRegisteredException extends RuntimeException {

    public ChatAlreadyRegisteredException(long chatId) {
        super("Чат id = %d уже зарегистрирован".formatted(chatId));
    }
}
