package edu.java.client;

import edu.java.client.link_information.LinkInfoReceiver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public abstract class AbstractClient<B> implements LinkInfoReceiver {

    protected final HttpServiceProxyFactory factory;

    public AbstractClient(String baseUrl) {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        factory = HttpServiceProxyFactory.builderFor(adapter).build();
    }
}
