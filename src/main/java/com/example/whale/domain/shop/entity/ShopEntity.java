package com.example.whale.domain.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.domain.shop.model.Shop;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long shopId;

	private String shopName;

	private String shopDescription;

	@Builder
	public ShopEntity(String shopName, String shopDescription) {
		this.shopName = shopName;
		this.shopDescription = shopDescription;
	}

	public static ShopEntity from(String shopName, String shopDescription) {
		return ShopEntity.builder()
			.shopName(shopName)
			.shopDescription(shopDescription)
			.build();
	}

	public static ShopEntity from(Shop shop) {
		return ShopEntity.builder()
			.shopName(shop.getShopName())
			.shopDescription(shop.getShopDescription())
			.build();
	}

	public void updateShopName(String shopName) {
		this.shopName = shopName;
	}

	public void updateShopDescription(String shopDescription) {
		this.shopDescription = shopDescription;
	}
}
