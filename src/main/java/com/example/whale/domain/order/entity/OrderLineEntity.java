package com.example.whale.domain.order.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.product.entity.ProductEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineEntity {

	@Id
	private String orderLineId; // ${orderId}:${productId}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductEntity product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	private Long orderQuantity;

	private BigDecimal totalAmount;

	private BigDecimal totalAmountBeforeDiscount;

	@Builder
	public OrderLineEntity(String orderLineId, ProductEntity product, OrderEntity order, Long orderQuantity,
		BigDecimal totalAmount, BigDecimal totalAmountBeforeDiscount) {
		this.orderLineId = orderLineId;
		this.product = product;
		this.order = order;
		this.orderQuantity = orderQuantity;
		this.totalAmount = totalAmount;
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
	}

	public static OrderLineEntity of(OrderLine orderLine) {
		return OrderLineEntity.builder()
			.orderLineId(orderLine.getOrderLineId())
			.product(ProductEntity.of(orderLine.getProduct()))
			.order(OrderEntity.of(orderLine.getOrder()))
			.orderQuantity(orderLine.getOrderQuantity())
			.totalAmount(orderLine.getTotalAmount())
			.totalAmountBeforeDiscount(orderLine.getTotalAmountBeforeDiscount())
			.build();
	}

	public static List<OrderLineEntity> collectToListOf(List<OrderLine> orderLines) {
		return orderLines.stream().map(OrderLineEntity::of).toList();
	}

}