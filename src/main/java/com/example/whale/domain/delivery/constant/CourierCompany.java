package com.example.whale.domain.delivery.constant;

import lombok.Getter;

@Getter
public enum CourierCompany {

	CJ_KOR_DELIVERY("CJ00"),
	KOREA_POST_DELIVERY("KP00"),
	HANJIN_DELIVERY("HJ00"),
	LOGEN_DELIVERY("LG00");

	private final String code;

	CourierCompany(String code) {
		this.code = code;
	}

}
