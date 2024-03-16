package edu.java.exception;



public class ChatNotAuthorizedException extends RuntimeException{
    public  ChatNotAuthorizedException (){
        super(String.format("Chat id = %d not authorized"));
    }
}
