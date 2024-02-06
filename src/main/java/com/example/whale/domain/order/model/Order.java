package com.example.whale.domain.order.model;

import java.math.BigDecimal;
import java.util.List;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.entity.OrderEntity;
import com.example.whale.domain.user.model.Customer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Order {

	private final String orderId;
	private final Customer customer;
	private final List<OrderLine> orderLines;
	private OrderStatus orderStatus;
	private final BigDecimal totalAmountOfOrder;

	@Builder
	public Order(String orderId, Customer customer, List<OrderLine> orderLines, OrderStatus orderStatus,
		BigDecimal totalAmountOfOrder) {
		this.orderId = orderId;
		this.customer = customer;
		this.orderLines = orderLines;
		this.orderStatus = orderStatus;
		this.totalAmountOfOrder = totalAmountOfOrder;
	}

	public static Order fromEntity(OrderEntity entity) {
		return Order.builder()
			.orderId(entity.getOrderId())
			.customer(Customer.fromEntity(entity.getCustomer()))
			.orderLines(OrderLine.toListFromEntity(entity.getOrderLines()))
			.totalAmountOfOrder(entity.getTotalAmountOfOrder())
			.build();
	}

}
