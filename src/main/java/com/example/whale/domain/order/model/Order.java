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
	private final Long customerId;
	private List<OrderLine> orderLines;
	private OrderStatus orderStatus;
	private BigDecimal totalAmountOfOrder;

	@Builder
	public Order(String orderId, Long customerId, List<OrderLine> orderLines, OrderStatus orderStatus,
		BigDecimal totalAmountOfOrder) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderLines = orderLines;
		this.orderStatus = orderStatus;
		this.totalAmountOfOrder = totalAmountOfOrder;
	}

	public static Order of(Customer customer) {
		return Order.builder()
			.orderId(generateOrderKey(customer.getUserId()))
			.customerId(customer.getUserId())
			.orderStatus(OrderStatus.NEW_ORDER)
			.build();
	}

	public static Order fromEntity(OrderEntity entity) {
		return Order.builder()
			.orderId(entity.getOrderId())
			.customerId(entity.getCustomerId())
			.orderLines(OrderLine.toListFromEntity(entity.getOrderLines()))
			.totalAmountOfOrder(entity.getTotalAmountOfOrder())
			.build();
	}

	private static String generateOrderKey(Long customerId) {
		return customerId.toString() + System.currentTimeMillis();
	}

	public void insertOrderLines(List<OrderLine> orderLines) {
		this.orderLines = orderLines;
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void calculateTotalAmountOfOrder() {
		this.totalAmountOfOrder =
			this.orderLines.stream().map(OrderLine::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public boolean isOrderLineNotEmpty() {
		return this.orderLines != null;
	}

}
