package edu.java.client;

import edu.java.service.BotService;

public class BotClient extends AbstractClient<BotService> {

    private static final String BOT_BASE_URL = "http://localhost:8090/";

    public BotClient() {
        this(BOT_BASE_URL);
    }

    public BotClient(String baseUrl) {
        super(baseUrl);
    }
}
