package com.example.whale.domain.order.model;

import java.math.BigDecimal;

import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.order.entity.OrderEntity;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {

	private final String orderId;
	private final Long customerId;
	private final BigDecimal totalAmountOfOrder;
	private final Address destination;

	@Builder
	@QueryProjection
	public Order(String orderId, Long customerId, BigDecimal totalAmountOfOrder, Address destination) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.totalAmountOfOrder = totalAmountOfOrder;
		this.destination = destination;
	}

	public static Order fromEntity(OrderEntity entity) {
		return Order.builder()
			.orderId(entity.getId())
			.customerId(entity.getCustomerId())
			.totalAmountOfOrder(entity.getTotalAmountOfOrder())
			.destination(Address.fromEntity(entity.getDestination()))
			.build();
	}

}
