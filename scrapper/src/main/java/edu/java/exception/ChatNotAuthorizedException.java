package edu.java.exception;

public class ChatNotAuthorizedException extends RuntimeException {
    public ChatNotAuthorizedException() {
        super("Chat id = %d not authorized");
    }
}
