package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.dto.ResponseLink;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ScrapperClient {
    private static final String CHAT_ENDPOINT = "/tg-chat";
    private static final String LINK_ENDPOINT = "/links";
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    public ListLinksResponse getLinks(Long chatId) {
        return webClient
            .get()
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(ResponseLink.class)
            .block();
    }

    public ResponseLink removeLink(Long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINK_ENDPOINT)
            .header(TG_CHAT_HEADER, chatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(ResponseLink.class)
            .block();
    }

    public void registerChat(Long chatId) {
        webClient
            .post()
            .uri(CHAT_ENDPOINT, chatId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public void deleteChat(Long chatId) {
        webClient
            .delete()
            .uri(CHAT_ENDPOINT, chatId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
