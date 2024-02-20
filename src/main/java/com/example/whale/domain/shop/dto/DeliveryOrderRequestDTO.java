package com.example.whale.domain.shop.dto;

import java.math.BigDecimal;

import com.example.whale.domain.delivery.constant.CourierCompany;

import lombok.Getter;

@Getter
public class DeliveryOrderRequestDTO {

	private final CourierCompany courierCompany;
	private final BigDecimal deliveryFee;
	private final String orderLineId;

	public DeliveryOrderRequestDTO(CourierCompany courierCompany, BigDecimal deliveryFee, String orderLineId) {
		this.courierCompany = courierCompany;
		this.deliveryFee = deliveryFee;
		this.orderLineId = orderLineId;
	}

}
