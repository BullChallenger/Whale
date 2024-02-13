package com.example.whale.domain.order.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.PersistableWrapper;
import com.example.whale.domain.order.model.Order;
import com.example.whale.domain.user.model.Customer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends PersistableWrapper {

	@Id
	@Column(name = "order_id", nullable = false, unique = true)
	private String id; // ${userId}:${시간}

	private Long customerId;

	@Embedded
	private OrderLineCollection orderLineCollection = new OrderLineCollection();

	private BigDecimal totalAmountOfOrder;

	@Builder
	public OrderEntity(String id, Long customerId, BigDecimal totalAmountOfOrder) {
		this.id = id;
		this.customerId = customerId;
		this.totalAmountOfOrder = totalAmountOfOrder;
	}

	public static OrderEntity of(Order order) {
		return OrderEntity.builder()
			.id(order.getOrderId())
			.customerId(order.getCustomerId())
			.totalAmountOfOrder(order.getTotalAmountOfOrder())
			.build();
	}

	public static OrderEntity of(Customer customer) {
		return OrderEntity.builder()
				.id(generateOrderKey(customer.getUserId()))
				.customerId(customer.getUserId())
				.build();
	}

	private static String generateOrderKey(Long customerId) {
		return customerId.toString() + ":" + System.currentTimeMillis();
	}

	public void calculateTotalAmountOfOrder() {
		this.totalAmountOfOrder = this.orderLineCollection.calculateTotalAmountOfOrder();
	}

}
