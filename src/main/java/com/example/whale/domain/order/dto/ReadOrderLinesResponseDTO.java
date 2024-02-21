package com.example.whale.domain.order.dto;

import java.math.BigDecimal;

import com.example.whale.domain.order.constant.OrderStatus;
import com.example.whale.domain.product.dto.ProductInfoInOrderLineDTO;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReadOrderLinesResponseDTO {

	private final String orderLineId;
	private final ProductInfoInOrderLineDTO product;
	private final Long orderQuantity;
	private final BigDecimal totalAmount;
	private final BigDecimal totalAmountBeforeDiscount;
	private final OrderStatus orderStatus;

	@Builder
	@QueryProjection
	public ReadOrderLinesResponseDTO(String orderLineId, ProductInfoInOrderLineDTO product, Long orderQuantity,
		BigDecimal totalAmount, BigDecimal totalAmountBeforeDiscount, OrderStatus orderStatus) {
		this.orderLineId = orderLineId;
		this.product = product;
		this.orderQuantity = orderQuantity;
		this.totalAmount = totalAmount;
		this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
		this.orderStatus = orderStatus;
	}

}
