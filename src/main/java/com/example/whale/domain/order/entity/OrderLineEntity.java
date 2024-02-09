package com.example.whale.domain.order.entity;

import com.example.whale.domain.common.entity.PersistableWrapper;
import java.math.BigDecimal;

import javax.persistence.Column;
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
public class OrderLineEntity extends PersistableWrapper {

	@Id
	@Column(name = "order_line_id", nullable = false, unique = true)
	private String id; // ${orderId}:${productId}

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
		this.id = orderLineId;
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

	public static OrderLineEntity of(String orderLineId, ProductEntity product, Long orderQuantity) {
		return OrderLineEntity.builder()
				.orderLineId(orderLineId)
				.product(product)
				.orderQuantity(orderQuantity)
				.totalAmount(product.getProductPrice().multiply(BigDecimal.valueOf(orderQuantity)))
				.build();
	}

	public void insertInOrder(OrderEntity order) {
		this.order = order;
	}

}
