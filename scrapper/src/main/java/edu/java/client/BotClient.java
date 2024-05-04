package edu.java.client;

import edu.java.dto.UpdateLink;
import edu.java.service.BotService;

public class BotClient extends AbstractClient<BotService> {

    public BotClient(String baseUrl) {
        super(baseUrl);
    }

    public void sendUpdate(UpdateLink update) {
        service.sendUpdate(update);
    }
}
