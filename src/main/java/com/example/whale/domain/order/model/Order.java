package com.example.whale.domain.order.model;

import java.math.BigDecimal;

import com.example.whale.domain.order.entity.OrderEntity;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {

	private final String orderId;
	private final Long customerId;
	private final BigDecimal totalAmountOfOrder;
	private final Long destinationId;

	@Builder
	@QueryProjection
	public Order(String orderId, Long customerId, BigDecimal totalAmountOfOrder, Long destinationId) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.totalAmountOfOrder = totalAmountOfOrder;
		this.destinationId = destinationId;
	}

	public static Order fromEntity(OrderEntity entity) {
		return Order.builder()
			.orderId(entity.getId())
			.customerId(entity.getCustomerId())
			.totalAmountOfOrder(entity.getTotalAmountOfOrder())
			.destinationId(entity.getDestinationId())
			.build();
	}

}
