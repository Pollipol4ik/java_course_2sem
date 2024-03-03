package edu.java.service;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import java.util.Collections;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    public ListLinksResponse getAllLinks(long chatId) {
        return new ListLinksResponse(Collections.emptyList());
    }

    public ResponseLink addLink(long chatId, AddLinkRequest addLinkRequest) {
        return new ResponseLink(0, null);
    }

    public ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return new ResponseLink(0, null);
    }
}
