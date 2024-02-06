package com.example.whale.domain.shop.model;

import com.example.whale.domain.shop.entity.ShopEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Shop {

	private final Long shopId;
	private String shopName;
	private String shopDescription;

	@Builder
	public Shop(Long shopId, String shopName, String shopDescription) {
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopDescription = shopDescription;
	}

	public static Shop fromEntity(ShopEntity entity) {
		return Shop.builder()
				.shopId(entity.getShopId())
				.shopName(entity.getShopName())
				.shopDescription(entity.getShopDescription())
				.build();
	}

}
