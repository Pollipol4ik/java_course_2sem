package edu.java.exception;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.RemoveLinkRequest;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException(RemoveLinkRequest removeLinkRequest) {
        super(String.format("Link id = %s not found", removeLinkRequest.linkId()));
    }

    public LinkNotFoundException(AddLinkRequest addLinkRequest) {
        super(String.format("Link url = %s not found", addLinkRequest.link()));
    }
}
