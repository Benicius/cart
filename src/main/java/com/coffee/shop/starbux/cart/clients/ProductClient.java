package com.coffee.shop.starbux.cart.clients;

import com.coffee.shop.starbux.cart.domains.Product;
import com.coffee.shop.starbux.cart.helpers.HeaderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class ProductClient {

    private final RestClient restClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductClient.class);

    private String url;

    @Autowired
    public ProductClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public CompletableFuture<Product> getProduct(
            final String name,
            final boolean hasTopping){

        final Map<String, String> headers =
                HeaderHelper.addParams(name);

        final UriComponentsBuilder builder =
                buildQueryParameters(name, hasTopping);

        try{

            final String urlParametrized = builder.build().encode().toUriString();

            final Product productResponse =
                    restClient.getForObject(urlParametrized, headers, Product.class);

            return CompletableFuture.completedFuture(productResponse);

        }catch (final HttpStatusCodeException e){
            LOGGER.error(e.getMessage(), e);
        }catch (final Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    private UriComponentsBuilder buildQueryParameters(
            final String name,
            final boolean hasTopping) {

        final String type;

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromUriString("http://localhost:8081".concat("/api/products/filter"));

        if (name != null){
            builder.queryParam("name", name);
        }

        if(hasTopping){
            type = "TOPPING";
            builder.queryParam("type", type);
        } else {
            type = "DRINK";
            builder.queryParam("type", type);
        }

        return builder;
    }
}
