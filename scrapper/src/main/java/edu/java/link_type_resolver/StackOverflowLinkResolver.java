package edu.java.link_type_resolver;

public class StackOverflowLinkResolver extends LinkTypeResolver {

    private static final String STACKOVERFLOW_PREFIX = "https://stackoverflow.com/";

    @Override
    public LinkType resolve(String link) {
        if (!link.startsWith(STACKOVERFLOW_PREFIX)) {
            return resolveNext(link);
        }
        return LinkType.STACKOVERFLOW;
    }
}
