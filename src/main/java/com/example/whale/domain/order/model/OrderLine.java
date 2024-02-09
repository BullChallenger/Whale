package com.example.whale.domain.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;

import com.example.whale.domain.order.entity.OrderLineEntity;
import com.example.whale.domain.product.model.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderLine {

	private final String orderLineId;
	private final Product product;
	private Order order;
	private final Long orderQuantity;
	private final BigDecimal totalAmount;
	private final BigDecimal totalAmountBeforeDiscount;

	@Builder
	public OrderLine(String orderLineId, Product product, Order order, Long orderQuantity) {
		this.orderLineId = orderLineId;
		this.product = product;
		this.order = order;
		this.orderQuantity = orderQuantity;
		this.totalAmount = calculateTotalAmount(product, orderQuantity);
		// TODO: 할인 적용 전 주문 금액 계산 로직 작성
		this.totalAmountBeforeDiscount = calculateTotalAmount(product, orderQuantity);
	}

	@QueryProjection
	public OrderLine(String orderLineId, Product product, Long orderQuantity, BigDecimal totalAmount,
					 BigDecimal totalAmountBeforeDiscount) {
		this.orderLineId = orderLineId;
		this.product = product;
		this.orderQuantity = orderQuantity;
		this.totalAmount = totalAmount;
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
	}

	public static OrderLine fromEntity(OrderLineEntity entity) {
		return OrderLine.builder()
			.orderLineId(entity.getId())
			.product(Product.fromEntity(entity.getProduct()))
			.order(Order.fromEntity(entity.getOrder()))
			.orderQuantity(entity.getOrderQuantity())
			.build();
	}

	public static OrderLine of(
		String orderLineId,
		Product product,
		Long orderQty
	) {
		return OrderLine.builder()
			.orderLineId(orderLineId)
			.product(product)
			.orderQuantity(orderQty)
			.build();
	}

	private BigDecimal calculateTotalAmount(Product product, Long orderQuantity) {
		return product.getProductPrice().getProductPrice().multiply(BigDecimal.valueOf(orderQuantity));
	}

}
