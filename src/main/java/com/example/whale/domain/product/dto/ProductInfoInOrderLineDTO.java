package com.example.whale.domain.product.dto;

import com.example.whale.domain.shop.model.Shop;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInfoInOrderLineDTO {

	private final String productId;
	private Shop provider;
	private String productName;
	private String productDescription;

	@Builder
	@QueryProjection
	public ProductInfoInOrderLineDTO(String productId, Shop provider, String productName, String productDescription) {
		this.productId = productId;
		this.provider = provider;
		this.productName = productName;
		this.productDescription = productDescription;
	}

}
