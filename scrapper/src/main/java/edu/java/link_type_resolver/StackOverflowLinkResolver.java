package edu.java.link_type_resolver;

public class StackOverflowLinkResolver extends LinkTypeResolver {

    private static final String STACK_OVERFLOW_PREFIX = "https://stackoverflow.com/";

    @Override
    public LinkType resolve(String link) {
        if (!link.startsWith(STACK_OVERFLOW_PREFIX)) {
            return resolveNext(link);
        }
        return LinkType.STACKOVERFLOW;
    }
}
