package edu.java.bot.controller;

import edu.java.bot.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({MissingRequestHeaderException.class, HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class})
    public ApiErrorResponse invalidRequest(RuntimeException e) {
        return new ApiErrorResponse(
            "Некорректный запрос",
            "400",
            "Invalid Request",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }
}
