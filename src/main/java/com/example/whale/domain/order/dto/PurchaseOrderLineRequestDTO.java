package com.example.whale.domain.order.dto;

import lombok.Getter;

@Getter
public class PurchaseOrderLineRequestDTO {

	private final String productId;
	private final Long quantity;

	public PurchaseOrderLineRequestDTO(String productId, Long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

}
