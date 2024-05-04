package edu.java.service;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    private final List<ResponseLink> links = new ArrayList<>();

    public ListLinksResponse getAllLinks(long chatId) {
        return new ListLinksResponse(links);
    }

    public ResponseLink addLink(long chatId, AddLinkRequest addLinkRequest) {
        for (ResponseLink link : links) {
            if (link.link().equals(addLinkRequest.link())) {
                throw new LinkAlreadyTrackedException(addLinkRequest);
            }
        }

        ResponseLink newLink = new ResponseLink(chatId, URI.create(addLinkRequest.link()));
        links.add(newLink);
        return newLink;
    }

    public ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        for (ResponseLink link : links) {
            if (link.linkId() == removeLinkRequest.linkId()) {
                links.remove(link);
                return link;
            }
        }
        throw new LinkNotFoundException(removeLinkRequest);
    }
}
