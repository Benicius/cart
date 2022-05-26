package com.coffee.shop.starbux.cart.service;

import com.coffee.shop.starbux.cart.domains.Cart;
import com.coffee.shop.starbux.cart.domains.ItemCart;
import com.coffee.shop.starbux.cart.domains.Product;
import com.coffee.shop.starbux.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart createCart(final List<String> names, final Cart cart){
        validateItems(cart.getItems());
        validateName(names);

        cart.getItems().forEach(itemCart -> {
            names.forEach(nameTemp ->{
                Product productResponse = productService.getProduct(nameTemp);
                itemCart.setProduct(productResponse);
            });

        });

        return cartRepository.save(cart);
    }

    private void validateItems(final List<ItemCart> items) {
        if (ObjectUtils.isEmpty(items)){
            return;
        }
        /*items.forEach(itemCart -> {
            if (!ObjectUtils.isEmpty(itemCart.getProduct().getName())){
                Product productResponse = productService.getProduct(itemCart.getProduct().getName());
                itemCart.setProduct(productResponse);
            }
        });*/
    }

    private void validateName(final List<String> name){
        if (name.isEmpty()){
            return;
        }
    }


}
