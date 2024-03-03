package edu.java.exception;

import edu.java.dto.AddLinkRequest;

public class LinkAlreadyTrackedException extends RuntimeException {

    public LinkAlreadyTrackedException(AddLinkRequest addLinkRequest) {
        super("Ссылка %s уже отслеживается".formatted(addLinkRequest.link()));
    }
}
