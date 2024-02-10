package com.example.whale.domain.order.model;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.entity.OrderEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {

	private final String orderId;
	private final Long customerId;
	private OrderStatus orderStatus;
	private final BigDecimal totalAmountOfOrder;

	@Builder
	@QueryProjection
	public Order(String orderId, Long customerId, OrderStatus orderStatus, BigDecimal totalAmountOfOrder) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderStatus = orderStatus;
		this.totalAmountOfOrder = totalAmountOfOrder;
	}

	public static Order fromEntity(OrderEntity entity) {
		return Order.builder()
			.orderId(entity.getId())
			.customerId(entity.getCustomerId())
			.orderStatus(entity.getOrderStatus())
			.totalAmountOfOrder(entity.getTotalAmountOfOrder())
			.build();
	}

}
