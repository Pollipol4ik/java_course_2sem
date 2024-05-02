package edu.java.exception;

import edu.java.dto.AddLinkRequest;
import org.springframework.http.HttpStatus;

public class LinkAlreadyTrackedException extends BaseException {

    public LinkAlreadyTrackedException(AddLinkRequest addLinkRequest) {
        super(String.format("The link %s is already being tracked", addLinkRequest.link()), HttpStatus.CONFLICT);
    }
}
