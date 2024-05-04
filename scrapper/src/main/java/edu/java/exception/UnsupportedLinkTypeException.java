package edu.java.exception;

import edu.java.dto.AddLinkRequest;

@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class UnsupportedLinkTypeException extends RuntimeException {

    public UnsupportedLinkTypeException(AddLinkRequest addLinkRequest) {
        super(String.format("The link type %s is not supported", addLinkRequest.link()));
    }

    public UnsupportedLinkTypeException(String link) {
        super(String.format("The link type %s is not supported", link));
    }
}
