package edu.java.exception;

import edu.java.dto.AddLinkRequest;

public class UnsupportedLinkTypeException extends RuntimeException {

    public UnsupportedLinkTypeException(AddLinkRequest addLinkRequest) {
        super(String.format("The link type %s is not supported", addLinkRequest.link()));
    }
}
