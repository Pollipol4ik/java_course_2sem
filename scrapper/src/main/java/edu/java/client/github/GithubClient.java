package edu.java.client.github;

import edu.java.client.AbstractClient;
import edu.java.client.dto.github.RepositoryResponse;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.link_type_resolver.LinkType;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubClient extends AbstractClient<GithubService> implements LinkInfoReceiver {

    private static final String DEFAULT_BASE_URL = "https://api.github.com/";
    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    public GithubClient() {
        this(DEFAULT_BASE_URL);
    }

    public GithubClient(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public LinkType getLinkType() {
        return LinkType.GITHUB;
    }

    @Override
    public LastUpdateTime receiveLastUpdateTime(URI url) {
        Matcher matcher = REPOSITORY_PATTERN.matcher(url.toString());
        if (!matcher.find()) {
            return null;
        }
        String owner = matcher.group(1);
        String repositoryName = matcher.group(2);
        RepositoryResponse response = service.getRepository(owner, repositoryName);
        return new LastUpdateTime(response.lastUpdate());
    }

    @Override
    public boolean isValidate(URI url) {
        return REPOSITORY_PATTERN.matcher(url.toString()).matches();
    }
}
