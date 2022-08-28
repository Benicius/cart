package com.coffee.shop.starbux.cart.controllers;

import com.coffee.shop.starbux.cart.domains.Cart;
import com.coffee.shop.starbux.cart.domains.CartRequest;
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
            @RequestBody final CartRequest cart){
        return new ResponseEntity<>(cartService.createCart(cart), HttpStatus.ACCEPTED);
    }
}
