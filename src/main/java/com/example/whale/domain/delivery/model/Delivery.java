package com.example.whale.domain.delivery.model;

import java.math.BigDecimal;

import com.example.whale.domain.delivery.constant.CourierCompany;
import com.example.whale.domain.delivery.entity.DeliveryEntity;
import com.example.whale.domain.order.model.OrderLine;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Delivery {

	private String id; // ${배송사 코드}:${송장번호}

	private CourierCompany courierCompany; // 택배사
	private String invoiceNumber; // 송장번호
	private BigDecimal fee; // 택배 요금

	private OrderLine orderLine;

	private String receiverName;
	private Address destination;

	@Builder
	public Delivery(String id, CourierCompany courierCompany, String invoiceNumber, BigDecimal fee, OrderLine orderLine,
		Address destination, String receiverName) {
		this.id = id;
		this.courierCompany = courierCompany;
		this.invoiceNumber = invoiceNumber;
		this.fee = fee;
		this.orderLine = orderLine;
		this.destination = destination;
		this.receiverName = receiverName;
	}

	public static Delivery fromEntity(DeliveryEntity entity, String receiverName) {
		return Delivery.builder()
				.id(entity.getId())
				.courierCompany(entity.getCourierCompany())
				.invoiceNumber(entity.getInvoiceNumber())
				.fee(entity.getFee())
				.orderLine(OrderLine.fromEntity(entity.getOrderLine()))
				.destination(Address.fromEntity(entity.getDestination()))
				.receiverName(receiverName)
			.build();
	}

}
