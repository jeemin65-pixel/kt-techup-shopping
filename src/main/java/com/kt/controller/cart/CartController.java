package com.kt.controller.cart;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.dto.cart.CartCreateRequest;
import com.kt.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
	private final CartService cartService;

	@PostMapping
	public ApiResult<Void> create(@Valid @RequestBody CartCreateRequest request) {
		cartService.create(request);

		return ApiResult.ok();
	}
}
