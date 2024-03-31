package edu.java.client;

import edu.java.client.link_information.LinkInfoReceiver;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractClient implements LinkInfoReceiver {
    protected final WebClient webClient;

    public AbstractClient(String apiUrl) {
        webClient = WebClient.create(apiUrl);
    }

}
