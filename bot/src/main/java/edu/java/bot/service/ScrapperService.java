package edu.java.bot.service;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.dto.ResponseLink;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperService {

    @GetExchange("/links")
    ListLinksResponse getAllLinks(@RequestHeader("Tg-Chat-Id") Long chatId);

    @PostExchange("/links")
    ResponseLink trackLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody AddLinkRequest addLinkRequest);

    @DeleteExchange("/links")
    ResponseLink untrackLink(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    );

    @PostExchange("/telegram/chat/{id}")
    void registerChat(@PathVariable("id") Long chatId);

    @DeleteExchange("/telegram/chat/{id}")
    void deleteChat(@PathVariable("id") Long chatId);
}
