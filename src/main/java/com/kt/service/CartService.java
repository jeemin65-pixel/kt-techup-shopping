package com.kt.service;

import org.springframework.stereotype.Service;

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
		// TODO: create 함수 개발
	}

}
