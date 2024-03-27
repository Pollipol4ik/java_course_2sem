package edu.java.dto;

public record ApiErrorResponse(String description, int code, String exceptionName, String exceptionMessage) {

}
