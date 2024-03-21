package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exception.ApiErrorException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ApiErrorResponse> apiErrorResponse(ApiErrorException e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            e.getDescription(),
            e.getCode(),
            e.getExceptionName(),
            e.getExceptionMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        ), HttpStatus.valueOf(e.getCode()));
    }
}
