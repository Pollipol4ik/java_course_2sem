package edu.java.dto;

import java.util.List;

public record ApiErrorResponse(String description, int code, String exceptionName, String exceptionMessage,
                               List<String> stacktrace) {

}
