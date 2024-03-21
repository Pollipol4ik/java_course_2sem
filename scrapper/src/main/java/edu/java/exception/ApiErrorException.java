package edu.java.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiErrorException extends RuntimeException {

    private final int code;
    private final String description;
    private final String exceptionName;
    private final String exceptionMessage;
}
