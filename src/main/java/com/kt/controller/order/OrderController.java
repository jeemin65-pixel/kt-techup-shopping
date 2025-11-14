package com.kt.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.ApiResult;
import com.kt.dto.order.OrderRequest;
import com.kt.dto.order.OrderStatusUpdateRequest;
import com.kt.dto.user.UserUpdatePasswordRequest;
import com.kt.security.CurrentUser;
import com.kt.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;

	// 주문 생성
	@PostMapping
	public ApiResult<Void> create(
		@AuthenticationPrincipal CurrentUser currentUser,
		@RequestBody @Valid OrderRequest.Create request) {
		orderService.create(
			currentUser.getId(),
			request.productId(),
			request.receiverName(),
			request.receiverAddress(),
			request.receiverMobile(),
			request.quantity()
		);
		return ApiResult.ok();
	}

	// 주문 상태 변경
	// @PatchMapping("{id}/complete")
	// public ApiResult<Void> complete(@PathVariable Long id) {
	// 	orderService.complete(id);
	//
	// 	return ApiResult.ok();
	// }
	//
	// @PatchMapping("{id}/shipped")
	// public ApiResult<Void> shipped(@PathVariable Long id) {
	// 	orderService.shipped(id);
	//
	// 	return ApiResult.ok();
	// }

	// CANCELLED("주문취소"),
	// SHIPPED("배송중"),
	// DELIVERED("배송완료"),
	// CONFIRMED("구매확정");

}
