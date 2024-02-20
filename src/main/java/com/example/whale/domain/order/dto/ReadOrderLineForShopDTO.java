package com.example.whale.domain.order.dto;

import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.order.model.OrderLine;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadOrderLineForShopDTO {

	private final OrderLine orderLine;
	private final Address address;

	@Builder
	@QueryProjection
	public ReadOrderLineForShopDTO(OrderLine orderLine, Address address) {
		this.orderLine = orderLine;
		this.address = address;
	}

}
