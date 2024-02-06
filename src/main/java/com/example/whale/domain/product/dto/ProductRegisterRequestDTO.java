package com.example.whale.domain.product.dto;

import java.math.BigDecimal;

import com.example.whale.domain.product.constant.SellStatus;

import lombok.Getter;

@Getter
public class ProductRegisterRequestDTO {

	private final Long shopId;
	private final String productName;
	private final BigDecimal productPrice;
	private final Long productStockQty;
	private final String productDescription;
	private final SellStatus sellStatus;

	public ProductRegisterRequestDTO(Long shopId, String productName, BigDecimal productPrice,
		Long productStockQty, String productDescription, SellStatus sellStatus) {
		this.shopId = shopId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productStockQty = productStockQty;
		this.productDescription = productDescription;
		this.sellStatus = sellStatus;
	}

}
