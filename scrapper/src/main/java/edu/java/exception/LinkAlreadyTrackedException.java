package edu.java.exception;

import edu.java.dto.AddLinkRequest;

public class LinkAlreadyTrackedException extends RuntimeException {

    public LinkAlreadyTrackedException(AddLinkRequest addLinkRequest) {
        super(String.format("The link %s is already being tracked", addLinkRequest.link()));
    }
}
