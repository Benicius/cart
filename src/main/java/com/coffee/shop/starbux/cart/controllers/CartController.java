package com.coffee.shop.starbux.cart.controllers;

import com.coffee.shop.starbux.cart.domains.Cart;
import com.coffee.shop.starbux.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Cart> createCart(
            @RequestHeader final String name,
            @RequestHeader(required = false) final boolean hasTopping,
            @RequestBody final Cart cart){
        return new ResponseEntity<>(cartService.createCart(name,hasTopping, cart), HttpStatus.ACCEPTED);
    }
}
