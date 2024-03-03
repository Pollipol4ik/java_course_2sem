package edu.java.client;

import edu.java.service.BotService;

public class BotClient extends AbstractClient<BotService> {

    private static final String BASE_URL = "http://localhost:8090/";

    public BotClient() {
        this(BASE_URL);
    }

    public BotClient(String baseUrl) {
        super(baseUrl);
    }
}
