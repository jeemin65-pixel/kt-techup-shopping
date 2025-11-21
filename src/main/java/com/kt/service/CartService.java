package com.kt.service;

import org.springframework.stereotype.Service;

import com.kt.domain.cart.Cart;
import com.kt.dto.cart.CartCreateRequest;
import com.kt.repository.cart.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	private final CartRepository cartRepository;

	public void create(CartCreateRequest request) {
		// Cart cart = new Cart(request.productCount(), request.productOption(), request.productId());

		// cartRepository.save(cart);
	}

}
