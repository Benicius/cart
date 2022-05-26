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

    public Product getProduct(String name){
        CompletableFuture<Product> product = productClient.getProduct(name);
        return product.join();
    }
}
