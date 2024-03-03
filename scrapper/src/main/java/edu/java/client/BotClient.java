package edu.java.client;

import edu.java.client.link_information.LastUpdateTime;
import edu.java.client.link_information.LinkInformationReceiverProvider;
import edu.java.link_type_resolver.LinkType;
import edu.java.service.BotService;

public class BotClient extends AbstractClient<BotService> {

    private static final String BASE_URL = "http://localhost:8090/";

    public BotClient() {
        this(BASE_URL);
    }

    public BotClient(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public LinkType getLinkType() {
        return null;
    }

    @Override
    public LastUpdateTime receiveLastUpdateTime(String link) {
        return null;
    }

    @Override
    public void registerMyself(LinkInformationReceiverProvider provider) {
        super.registerMyself(provider);
    }
}
