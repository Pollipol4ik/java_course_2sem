package edu.java.client;

import java.lang.reflect.ParameterizedType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public abstract class AbstractClient<S> {

    protected final S service;

    public AbstractClient(String baseUrl) {
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        Class<S> serviceClass = getServiceClass();
        service = httpServiceProxyFactory.createClient(serviceClass);
    }

    @SuppressWarnings("unchecked")
    private Class<S> getServiceClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<S>) parameterizedType.getActualTypeArguments()[0];
    }
}
