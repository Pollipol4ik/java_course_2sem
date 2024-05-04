package edu.java.client.link_information;

import edu.java.link_type_resolver.LinkType;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public interface LinkInfoReceiver {

    LinkType getLinkType();

    List<LinkInfo> receiveLastUpdateTime(URI url);

    boolean isValidate(URI url);

    @Autowired
    default void registerMyself(LinkInformationReceiverProvider provider) {
        provider.registerReceiver(getLinkType(), this);
    }
}
