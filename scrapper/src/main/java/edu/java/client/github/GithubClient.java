package edu.java.client.github;

import edu.java.client.AbstractClient;
import edu.java.client.github.events.EventProvider;
import edu.java.client.github.events.GitHubEvent;
import edu.java.client.link_information.LinkInfo;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import io.github.resilience4j.retry.annotation.Retry;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
public class GithubClient extends AbstractClient {

    private static final String DEFAULT_BASE_URL = "https://api.github.com/repos";
    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");
    private static final String EVENTS_ENDPOINT = "/events?per_page=10";
    private final List<EventProvider> eventProviders;
    @Value("${client.github.token}")
    private String token;

    public GithubClient(List<EventProvider> eventProviders) {
        this(DEFAULT_BASE_URL, eventProviders);
    }

    public GithubClient(String baseUrl, List<EventProvider> eventProviders) {
        super(baseUrl);
        this.eventProviders = eventProviders;
    }

    @Override
    public LinkType getLinkType() {
        return LinkType.GITHUB;
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Override
    @Retry(name = "basic")
    public List<LinkInfo> receiveLastUpdateTime(URI url) {
        if (!isValidate(url)) {
            return null;
        }
        GitHubEvent[] info = webClient
            .get()
            .uri(url.getPath() + EVENTS_ENDPOINT)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(GitHubEvent[].class)
            .onErrorReturn(new GitHubEvent[0])
            .block();
        if (info == null || info.length == 0) {
            String response = webClient
                .get()
                .uri(url.getPath())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorReturn("")
                .block();
            if (response.isEmpty()) {
                throw new UnsupportedLinkTypeException(url.toString());
            }
            return Collections.emptyList();
        }
        return Arrays.stream(info)
            .flatMap(event -> eventProviders.stream()
                .filter(provider -> provider.checkType(event.type()))
                .map(provider -> new LinkInfo(url, provider.getMessage(event), event.updateTime()))
            )
            .toList();
    }

    @Override
    @Retry(name = "basic")
    public boolean isValidate(URI url) {
        return REPOSITORY_PATTERN.matcher(url.toString()).matches();
    }
}
