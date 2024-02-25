package edu.java.client.github;

import edu.java.client.AbstractClient;
import edu.java.client.dto.github.RepositoryResponse;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.link_type_resolver.LinkType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubClient extends AbstractClient {

    private static final String GITHUB_API_BASE_URL = "https://api.github.com/";
    private static final Pattern GITHUB_REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");
    private final GithubService service;

    public GithubClient() {
        this(GITHUB_API_BASE_URL);
    }

    public GithubClient(String baseUrl) {
        super(baseUrl);
        service = factory.createClient(GithubService.class);
    }

    @Override
    public LinkType getLinkType() {
        return LinkType.GITHUB;
    }

    @Override
    public LastUpdateTime receiveLastUpdateTime(String link) {
        Matcher matcher = GITHUB_REPOSITORY_PATTERN.matcher(link);
        if (!matcher.find()) {
            return null;
        }
        RepositoryResponse response = service.getRepository(matcher.group(1), matcher.group(2));
        return new LastUpdateTime(response.lastUpdate());
    }
}
