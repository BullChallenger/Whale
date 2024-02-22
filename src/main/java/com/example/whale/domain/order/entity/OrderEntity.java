package com.example.whale.domain.order.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.PersistableWrapper;
import com.example.whale.domain.delivery.entity.AddressEntity;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private AddressEntity destination;

	@Builder
	public OrderEntity(String id, Long customerId, BigDecimal totalAmountOfOrder, AddressEntity destination) {
		this.id = id;
		this.customerId = customerId;
		this.totalAmountOfOrder = totalAmountOfOrder;
		this.destination = destination;
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

	public void setDestinationForDelivery(AddressEntity destination) {
		this.destination = destination;
	}

}
