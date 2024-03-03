package edu.java.exception;

import edu.java.dto.RemoveLinkRequest;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(RemoveLinkRequest removeLinkRequest) {
        super("Ссылка id = %s не найдена".formatted(removeLinkRequest.linkId()));
    }
}
