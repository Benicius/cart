package com.coffee.shop.starbux.cart.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    private final RestTemplate restTemplate;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T getForObject(
            final String url,
            final Map<String, String> headerParams,
            final Class<T> expectedResponseType){

        final HttpHeaders headers = new HttpHeaders();
        headers.setAll(headerParams);
        return exchange(url, expectedResponseType, new HttpEntity<>(headers), HttpMethod.GET);
    }

    private <T> T exchange(
            final String url,
            final Class<T> expectedResponseType,
            final HttpEntity<Object> httpEntity,
            final HttpMethod method) {

        LOGGER.debug("Doing a {} on url {}", method, url);
        return restTemplate.exchange(url, method, httpEntity, expectedResponseType).getBody();
    }
}
