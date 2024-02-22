package com.example.whale.domain.shop.dto;

import lombok.Getter;

@Getter
public class UpdateShopInfoDTO {
	
	private final Long shopId;
	private final String shopName;
	private final String shopDescription;

	public UpdateShopInfoDTO(Long shopId, String shopName, String shopDescription) {
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopDescription = shopDescription;
	}

}
