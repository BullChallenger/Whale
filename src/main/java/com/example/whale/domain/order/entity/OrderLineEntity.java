package com.example.whale.domain.order.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.whale.domain.common.entity.PersistableWrapper;
import com.example.whale.domain.order.constant.OrderStatus;
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

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Builder
	public OrderLineEntity(String orderLineId, ProductEntity product, OrderEntity order, Long orderQuantity,
		BigDecimal totalAmount, BigDecimal totalAmountBeforeDiscount, OrderStatus orderStatus) {
		this.id = orderLineId;
		this.product = product;
		this.order = order;
		this.orderQuantity = orderQuantity;
		this.totalAmount = totalAmount;
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
		this.orderStatus = orderStatus;
	}

	public static OrderLineEntity of(String orderLineId, ProductEntity product, Long orderQuantity) {
		return OrderLineEntity.builder()
				.orderLineId(orderLineId)
				.product(product)
				.orderQuantity(orderQuantity)
				.totalAmount(product.getProductPrice().multiply(BigDecimal.valueOf(orderQuantity)))
				.orderStatus(OrderStatus.NEW_ORDER)
				.build();
	}

	public void insertInOrder(OrderEntity order) {
		this.order = order;
	}

	public void updateOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

}
