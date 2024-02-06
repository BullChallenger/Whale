package com.example.whale.domain.shop.dto;

import lombok.Getter;

@Getter
public class ShopRegisterRequestDTO {

	private final String shopName;
	private final String shopDescription;

	public ShopRegisterRequestDTO(String shopName, String shopDescription) {
		this.shopName = shopName;
		this.shopDescription = shopDescription;
	}

}
