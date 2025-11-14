package com.kt.dto.order;

import com.kt.domain.order.OrderStatus;

public record OrderStatusUpdateRequest(
	Long id,
	OrderStatus currentStatus
) {
}
