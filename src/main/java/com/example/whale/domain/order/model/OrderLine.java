package com.example.whale.domain.order.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.product.model.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderLine {

	private final String orderLineId;
	private final Product product;
	private final Order order;
	private final Long orderQuantity;
	private final BigDecimal totalAmount;
	private final BigDecimal totalAmountBeforeDiscount;

	@Builder
	public OrderLine(String orderLineId, Product product, Order order, Long orderQuantity, BigDecimal totalAmount,
		BigDecimal totalAmountBeforeDiscount) {
		this.orderLineId = orderLineId;
		this.product = product;
		this.order = order;
		this.orderQuantity = orderQuantity;
		this.totalAmount = totalAmount;
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
	}

	public static OrderLine fromEntity(OrderLineEntity entity) {
		return OrderLine.builder()
			.orderLineId(entity.getOrderLineId())
			.product(Product.fromEntity(entity.getProduct()))
			.order(Order.fromEntity(entity.getOrder()))
			.orderQuantity(entity.getOrderQuantity())
			.totalAmount(entity.getTotalAmount())
			.totalAmountBeforeDiscount(entity.getTotalAmountBeforeDiscount())
			.build();
	}

	public static List<OrderLine> toListFromEntity(List<OrderLineEntity> orderLines) {
		return orderLines.stream().map(OrderLine::fromEntity).collect(Collectors.toList());
	}

}
