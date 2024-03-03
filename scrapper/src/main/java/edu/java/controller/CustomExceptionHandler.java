package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @ExceptionHandler(ChatAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse chatAlreadyRegisteredException(ChatAlreadyRegisteredException e) {
        return new ApiErrorResponse(
            "Чат уже зарегистрирован",
            "409",
            "Chat already registered",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatNotFoundException(ChatNotFoundException e) {
        return new ApiErrorResponse(
            "Чат не найден",
            "404",
            "Chat not found",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFoundException(LinkNotFoundException e) {
        return new ApiErrorResponse(
            "Ссылка не найдена",
            "404",
            "Link not found",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(UnsupportedLinkTypeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse unsupportedLinkException(UnsupportedLinkTypeException e) {
        return new ApiErrorResponse(
            "Ссылка не поддерживается",
            "422",
            "Unsupported link",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }

    @ExceptionHandler(LinkAlreadyTrackedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse linkAlreadyTrackedException(LinkAlreadyTrackedException e) {
        return new ApiErrorResponse(
            "Ссылка уже отслеживается",
            "409",
            "Link is already tracked",
            e.getMessage(),
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString)
                .collect(Collectors.toList())
        );
    }

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
