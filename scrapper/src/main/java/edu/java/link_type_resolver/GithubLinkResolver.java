package edu.java.link_type_resolver;

public class GithubLinkResolver extends LinkTypeResolver {

    private static final String GITHUB_PREFIX = "https://github.com/";

    @Override
    public LinkType resolve(String link) {
        if (!link.startsWith(GITHUB_PREFIX)) {
            return resolveNext(link);
        }
        return LinkType.GITHUB;
    }
}
