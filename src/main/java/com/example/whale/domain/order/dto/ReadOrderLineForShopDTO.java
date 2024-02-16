package com.example.whale.domain.order.dto;

import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.order.model.OrderLine;
import com.example.whale.domain.user.model.Customer;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadOrderLineForShopDTO {

	private final OrderLine orderLine;
	private final Customer receiver;
	private final Address address;

	@Builder
	@QueryProjection
	public ReadOrderLineForShopDTO(OrderLine orderLine, Customer receiver, Address address) {
		this.orderLine = orderLine;
		this.receiver = receiver;
		this.address = address;
	}

}
