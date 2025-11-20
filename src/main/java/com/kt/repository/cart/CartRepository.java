package com.kt.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
