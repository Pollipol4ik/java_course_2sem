package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exception.BaseException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ApiErrorResponse handleBaseException(BaseException e) {
        return createErrorResponse(e.getMessage(), e.getHttpStatus(), e);
    }

    private ApiErrorResponse createErrorResponse(String message, HttpStatus httpStatus, RuntimeException e) {
        return new ApiErrorResponse(
            message,
            String.valueOf(httpStatus.value()),
            httpStatus.getReasonPhrase(),
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList())
        );
    }
}
