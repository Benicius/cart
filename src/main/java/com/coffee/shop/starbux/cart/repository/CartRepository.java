package com.coffee.shop.starbux.cart.repository;

import com.coffee.shop.starbux.cart.domains.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Long> {
}
