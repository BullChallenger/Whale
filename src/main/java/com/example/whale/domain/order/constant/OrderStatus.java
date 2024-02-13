package com.example.whale.domain.order.constant;

import lombok.Getter;

@Getter
public enum OrderStatus {

	/** 신규 주문 : 결제 완료 후, 주문 확인 전 주문 **/
	NEW_ORDER("NEW_ORDER"),
	/** 발송 대기 : 주문 확인 후, 발송 처리 전 주문 **/
	WAITING_DELIVERY("WAITING_DELIVERY"),
	/** 배송중 : 배송 완료 후, 구매확정 전 주문 **/
	DELIVERY("DELIVERY"),
	/** 배송 완료 : 배송 완료 후, 구매확정 전 주문 **/
	DELIVERY_COMPLETE("DELIVERY_COMPLETE"),
	/** 구매 확정 : 구매확정 되어 정산예정이거나 정산 완료된 주문 **/
	PURCHASE_CONFIRM("PURCHASE_CONFIRM"),
	/** 취소 요청 : 발송 대기 주문에 대해 구매자가 취소를 원하는 주문 **/
	CANCEL_ORDER("CANCEL_ORDER"),
	/** 교환 요청 : 배송중 이후 주문에 대해 구매자가 교환을 원하는 주문 **/
	EXCHANGE("EXCHANGE"),
	/** 반품 요청 : 배송중 이후 주문에 대해 구매자가 반품을 원하는 주문 **/
	RETURN("RETURN")
	;

	private final String status;

	OrderStatus(String status) {
		this.status = status;
	}
}
