package com.example.whale.domain.order.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.order.model.Order;

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
public class OrderEntity extends BaseEntity {

	@Id
	private String orderId; // ${userId}:${시간}

	private Long customerId;

	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
	private List<OrderLineEntity> orderLines;

	private OrderStatus orderStatus;

	private BigDecimal totalAmountOfOrder;

	@Builder
	public OrderEntity(String orderId, Long customerId, List<OrderLineEntity> orderLines, OrderStatus orderStatus,
		BigDecimal totalAmountOfOrder) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderLines = orderLines;
		this.orderStatus = orderStatus;
		this.totalAmountOfOrder = totalAmountOfOrder;
	}

	public static OrderEntity of(Order order) {
		return OrderEntity.builder()
			.orderId(order.getOrderId())
			.customerId(order.getCustomerId())
			.orderLines(OrderLineEntity.collectToListOf(order.getOrderLines()))
			.orderStatus(OrderStatus.NEW_ORDER)
			.totalAmountOfOrder(order.getTotalAmountOfOrder())
			.build();
	}

}
