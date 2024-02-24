package edu.java.client.link_information;

import edu.java.link_type_resolver.LinkType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LinkInformationReceiverProvider {

    private final Map<LinkType, LinkInformationReceiver> receivers = new HashMap<>();

    public void registerReceiver(LinkType linkType, LinkInformationReceiver informationReceiver) {
        receivers.put(linkType, informationReceiver);
    }

    public LinkInformationReceiver getReceiver(LinkType linkType) {
        return receivers.getOrDefault(linkType, null);
    }
}
