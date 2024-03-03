package edu.java.bot.client;

import java.lang.reflect.ParameterizedType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public abstract class AbstractClient<S> {

    protected final S service;

    @SuppressWarnings("unchecked")
    public AbstractClient(String baseUrl) {
        var factory = HttpServiceProxyFactory.builderFor(
            WebClientAdapter.create(
                WebClient.builder()
                    .baseUrl(baseUrl)
                    .build()
            )
        ).build();
        var serviceClass = (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments()[0];
        service = factory.createClient(serviceClass);
    }
}
