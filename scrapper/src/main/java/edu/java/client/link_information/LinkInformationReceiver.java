package edu.java.client.link_information;

import edu.java.link_type_resolver.LinkType;
import org.springframework.beans.factory.annotation.Autowired;

public interface LinkInformationReceiver {

    LinkType getLinkType();

    LastUpdateTime receiveLastUpdateTime(String link);

    @Autowired
    default void registerMyself(LinkInformationReceiverProvider provider) {
        provider.registerReceiver(getLinkType(), this);
    }
}
