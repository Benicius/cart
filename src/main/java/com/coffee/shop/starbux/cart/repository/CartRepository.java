package com.coffee.shop.starbux.cart.repository;

import com.coffee.shop.starbux.cart.domains.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
