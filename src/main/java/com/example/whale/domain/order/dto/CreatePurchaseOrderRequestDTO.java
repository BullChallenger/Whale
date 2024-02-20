package com.example.whale.domain.order.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CreatePurchaseOrderRequestDTO {

	private final Long userId;
	private final List<PurchaseOrderLineRequestDTO> orderLines;
	private final Long destinationId;

	public CreatePurchaseOrderRequestDTO(Long userId, List<PurchaseOrderLineRequestDTO> orderLines, Long destinationId) {
		this.userId = userId;
		this.orderLines = orderLines;
		this.destinationId = destinationId;
	}

}
