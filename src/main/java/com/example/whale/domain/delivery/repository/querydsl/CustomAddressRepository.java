package com.example.whale.domain.delivery.repository.querydsl;

import static com.example.whale.domain.delivery.entity.QAddressEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.whale.domain.delivery.model.Address;
import com.example.whale.domain.delivery.model.QAddress;
import com.example.whale.domain.user.model.QCustomer;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomAddressRepository {

	private final JPAQueryFactory queryFactory;

	public List<Address> findAddressesByUserId(Long userId) {
		return queryFactory.select(
			new QAddress(
				addressEntity.id,
				new QCustomer(
					addressEntity.receiver.id,
					addressEntity.receiver.email,
					addressEntity.receiver.username,
					addressEntity.receiver.nickname
				),
				addressEntity.zipcode,
				addressEntity.address,
				addressEntity.detailAddress
			)
		).from(addressEntity)
		.where(addressEntity.receiver.id.eq(userId))
		.fetch();
	}

}
