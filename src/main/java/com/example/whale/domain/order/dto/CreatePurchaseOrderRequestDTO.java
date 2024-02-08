package com.example.whale.domain.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CreatePurchaseOrderRequestDTO {

	private final Long userId;
	private final List<PurchaseOrderLineRequestDTO> orderLines;

	public CreatePurchaseOrderRequestDTO(Long userId, List<PurchaseOrderLineRequestDTO> orderLines) {
		this.userId = userId;
		this.orderLines = orderLines;
	}

}
