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

    @ExceptionHandler(ChatAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleChatAlreadyRegisteredException(ChatAlreadyRegisteredException e) {
        return createErrorResponse("Chat already registered", HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleChatNotFoundException(ChatNotFoundException e) {
        return createErrorResponse("Chat not found", HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleLinkNotFoundException(LinkNotFoundException e) {
        return createErrorResponse("Link not found", HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(UnsupportedLinkTypeException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse handleUnsupportedLinkException(UnsupportedLinkTypeException e) {
        return createErrorResponse("Unsupported link", HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(LinkAlreadyTrackedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleLinkAlreadyTrackedException(LinkAlreadyTrackedException e) {
        return createErrorResponse("Link is already tracked", HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({MissingRequestHeaderException.class, HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidRequest(RuntimeException e) {
        return createErrorResponse("Invalid Request", HttpStatus.BAD_REQUEST, e);
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
