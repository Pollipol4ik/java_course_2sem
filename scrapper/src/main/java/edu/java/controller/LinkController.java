package edu.java.controller;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
@Log4j2
public class LinkController {

    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getAllLinks(@RequestHeader(TG_CHAT_ID_HEADER) long chatId) {
        log.info("Get all links for chat with id {}", chatId);
        return linkService.getAllLinks(chatId);
    }

    @PostMapping
    public ResponseLink addLink(
        @RequestHeader(TG_CHAT_ID_HEADER) long chatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("Add link to chat with id {}", chatId);
        return linkService.addLink(chatId, addLinkRequest);
    }

    @DeleteMapping
    public ResponseLink removeLink(
        @RequestHeader(TG_CHAT_ID_HEADER) long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("Remove link from chat with id {}", chatId);
        return linkService.removeLink(chatId, removeLinkRequest);
    }
}
