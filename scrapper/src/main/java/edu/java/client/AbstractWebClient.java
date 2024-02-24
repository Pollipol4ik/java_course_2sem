package edu.java.client;

import edu.java.client.link_information.LinkInformationReceiver;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public abstract class AbstractWebClient implements LinkInformationReceiver {

    protected final HttpServiceProxyFactory factory;

    public AbstractWebClient(String baseUrl) {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        factory = HttpServiceProxyFactory.builderFor(adapter).build();
    }
}
