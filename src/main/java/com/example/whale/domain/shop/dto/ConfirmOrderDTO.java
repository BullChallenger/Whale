package com.example.whale.domain.shop.dto;

import lombok.Getter;

@Getter
public class ConfirmOrderDTO {

	private final Long shopId;
	private final String orderLineId;
	private final String status;

	public ConfirmOrderDTO(Long shopId, String orderLineId, String status) {
		this.shopId = shopId;
		this.orderLineId = orderLineId;
		this.status = status;
	}

}
