package com.example.whale.domain.shop.repository.querydsl;

import static com.example.whale.domain.article.entity.QArticleEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomShopRepository {

	private final JPAQueryFactory queryFactory;

	public boolean isShopExists(Long shopId) {
		return queryFactory
			.selectOne()
			.from(shopEntity)
			.where(shopEntity.shopId.eq(shopId))
			.fetchFirst() != null;
	}


	public boolean isSameShopNameAlreadyExists(String shopName) {
		return queryFactory
			.selectOne()
			.from(shopEntity)
			.where(shopEntity.shopName.eq(shopName))
			.fetchFirst() != null;
	}

}
