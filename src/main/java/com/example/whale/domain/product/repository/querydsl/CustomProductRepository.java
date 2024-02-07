package com.example.whale.domain.product.repository.querydsl;

import static com.example.whale.domain.product.entity.QProductEntity.*;
import static com.example.whale.domain.shop.entity.QShopEntity.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.whale.domain.product.model.Product;
import com.example.whale.domain.product.model.QProduct;
import com.example.whale.domain.shop.model.QShop;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomProductRepository {

	private final JPAQueryFactory queryFactory;

	public boolean isProductExists(String productId) {
		return queryFactory.selectOne()
			.from(productEntity)
			.where(productEntity.productId.eq(productId))
			.fetchFirst() != null;
	}

	public Optional<Product> readProductDetailsById(String productId) {
		return Optional.ofNullable(
			queryFactory.select(
				new QProduct(
					productEntity.productId,
					new QShop(
						shopEntity.shopId,
						shopEntity.shopName,
						shopEntity.shopDescription
					),
					productEntity.productName,
					productEntity.productPrice,
					productEntity.productStockQty,
					productEntity.productDescription,
					productEntity.sellStatus
				)
			).from(productEntity)
			.innerJoin(productEntity.provider, shopEntity)
			.where(productEntity.productId.eq(productId))
			.fetchOne()
		);
	}

}
