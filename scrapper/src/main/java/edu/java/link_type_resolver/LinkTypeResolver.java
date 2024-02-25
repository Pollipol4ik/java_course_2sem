package edu.java.link_type_resolver;

public abstract class LinkTypeResolver {

    private LinkTypeResolver next;

    public static LinkTypeResolver link(LinkTypeResolver first, LinkTypeResolver... chain) {
        LinkTypeResolver head = first;
        for (LinkTypeResolver resolver : chain) {
            head.next = resolver;
            head = resolver;
        }
        return first;
    }

    public abstract LinkType resolve(String link);

    protected LinkType resolveNext(String link) {
        if (next == null) {
            return LinkType.UNKNOWN;
        }
        return next.resolve(link);
    }
}
