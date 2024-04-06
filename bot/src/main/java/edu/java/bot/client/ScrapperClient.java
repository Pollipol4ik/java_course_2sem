package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.dto.ResponseLink;
import edu.java.bot.service.ScrapperService;
import io.github.resilience4j.retry.annotation.Retry;

public class ScrapperClient extends AbstractClient<ScrapperService> {

    private static final String SCRAPPER_BASE_URL = "http://localhost:8080/";

    public ScrapperClient() {
        this(SCRAPPER_BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        super(baseUrl);
    }

    @Retry(name = "basic")
    public ListLinksResponse getAllTrackedLinks(long chatId) {
        return service.getAllLinks(chatId);
    }

    @Retry(name = "basic")
    public ResponseLink trackLink(long chatId, AddLinkRequest addLinkRequest) {
        return service.trackLink(chatId, addLinkRequest);
    }

    @Retry(name = "basic")
    public ResponseLink untrackLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return service.untrackLink(chatId, removeLinkRequest);
    }

    @Retry(name = "basic")
    public void registerChat(long chatId) {
        service.registerChat(chatId);
    }

    @Retry(name = "basic")
    public void deleteChat(long chatId) {
        service.deleteChat(chatId);
    }

}
