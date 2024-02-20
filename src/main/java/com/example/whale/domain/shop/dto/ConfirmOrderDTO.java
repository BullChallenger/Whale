package com.example.whale.domain.shop.dto;

import com.example.whale.domain.order.constant.OrderStatus;

import lombok.Getter;

@Getter
public class ConfirmOrderDTO {

	private final Long shopId;
	private final String orderLineId;
	private final OrderStatus status;

	public ConfirmOrderDTO(Long shopId, String orderLineId, OrderStatus status) {
		this.shopId = shopId;
		this.orderLineId = orderLineId;
		this.status = status;
	}

}
