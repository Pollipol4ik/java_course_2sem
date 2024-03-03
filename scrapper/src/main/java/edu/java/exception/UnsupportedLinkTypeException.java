package edu.java.exception;

import edu.java.dto.AddLinkRequest;

public class UnsupportedLinkTypeException extends RuntimeException {

    public UnsupportedLinkTypeException(AddLinkRequest addLinkRequest) {
        super("Тип ссылки %s не поддерживается".formatted(addLinkRequest.link()));
    }
}
