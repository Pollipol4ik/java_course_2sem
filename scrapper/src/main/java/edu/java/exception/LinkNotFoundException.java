package edu.java.exception;

import edu.java.dto.RemoveLinkRequest;
import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends BaseException {

    public LinkNotFoundException(RemoveLinkRequest removeLinkRequest) {
        super(String.format("Link id = %s not found", removeLinkRequest.linkId()), HttpStatus.NOT_FOUND);
    }
}
