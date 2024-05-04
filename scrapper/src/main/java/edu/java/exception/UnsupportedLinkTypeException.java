package edu.java.exception;

import edu.java.dto.AddLinkRequest;
import org.springframework.http.HttpStatus;

public class UnsupportedLinkTypeException extends BaseException {

    public UnsupportedLinkTypeException(AddLinkRequest addLinkRequest) {
        super(String.format("The link type %s is not supported", addLinkRequest.link()), HttpStatus.BAD_REQUEST);
    }
}
