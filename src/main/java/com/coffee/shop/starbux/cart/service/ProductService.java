package com.coffee.shop.starbux.cart.service;

import com.coffee.shop.starbux.cart.clients.ProductClient;
import com.coffee.shop.starbux.cart.domains.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private final ProductClient productClient;

    @Autowired
    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public Product getProduct(final String name, final boolean hasTopping){
        CompletableFuture<Product> product = productClient.getProduct(name, hasTopping);
        return product.join();
    }
}
