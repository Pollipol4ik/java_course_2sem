package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.dto.ResponseLink;
import edu.java.bot.service.ScrapperService;

public class ScrapperClient extends AbstractClient<ScrapperService> {

    private static final String SCRAPPER_BASE_URL = "http://localhost:8080/";

    public ScrapperClient() {
        this(SCRAPPER_BASE_URL);
    }

    public ScrapperClient(String baseUrl) {
        super(baseUrl);
    }

    public ListLinksResponse getAllTrackedLinks(long chatId) {
        return service.getAllLinks(chatId);
    }

    public ResponseLink trackLink(long chatId, AddLinkRequest addLinkRequest) {
        return service.trackLink(chatId, addLinkRequest);
    }

    public ResponseLink untrackLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return service.untrackLink(chatId, removeLinkRequest);
    }

    public void registerChat(long chatId) {
        service.registerChat(chatId);
    }

    public void deleteChat(long chatId) {
        service.deleteChat(chatId);
    }

}
