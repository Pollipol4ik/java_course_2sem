package edu.java.controller;

import edu.java.dto.ApiErrorResponse;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({MissingRequestHeaderException.class, HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidRequest(RuntimeException e) {
        return createErrorResponse("Invalid Request", HttpStatus.BAD_REQUEST, e);
    }

    private ApiErrorResponse createErrorResponse(String message, HttpStatus httpStatus, RuntimeException e) {
        return new ApiErrorResponse(
            message,
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            e.getMessage()
        );
    }

    @ExceptionHandler(ChatAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleChatAlreadyRegisteredException(ChatAlreadyRegisteredException e) {
        return createErrorResponse("Чат уже зарегистрирован!", HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleChatNotFoundException(ChatNotFoundException e) {
        return createErrorResponse("Чат не найден!", HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleLinkNotFoundException(LinkNotFoundException e) {
        return createErrorResponse("Ссылка не найдена", HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(UnsupportedLinkTypeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse handleUnsupportedLinkException(UnsupportedLinkTypeException e) {
        return createErrorResponse("Неподдерживаемая ссылка.", HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(LinkAlreadyTrackedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleLinkAlreadyTrackedException(LinkAlreadyTrackedException e) {
        return createErrorResponse("Ссылка уже отслеживается!", HttpStatus.CONFLICT, e);
    }

}
