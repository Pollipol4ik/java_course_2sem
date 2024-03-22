package edu.java.service.link;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import org.springframework.stereotype.Service;

@Service
public interface LinkService {

    ListLinksResponse getAllLinks(Long chatId);

    ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest);

    ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest);

}
