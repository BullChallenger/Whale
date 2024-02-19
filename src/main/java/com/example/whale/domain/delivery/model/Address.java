package com.example.whale.domain.delivery.model;

import com.example.whale.domain.delivery.entity.AddressEntity;
import com.example.whale.domain.user.model.Customer;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Address {

	private final Long id;

	private final Customer customer;

	private final String zipcode; // 우편번호
	private final String address; // 거주지
	private final String detailAddress; // 상세 주소

	@Builder
	@QueryProjection
	public Address(Long id, Customer customer, String zipcode, String address, String detailAddress) {
		this.id = id;
		this.customer = customer;
		this.zipcode = zipcode;
		this.address = address;
		this.detailAddress = detailAddress;
	}

	public static Address fromEntity(AddressEntity entity) {
		return Address.builder()
			.id(entity.getId())
			.customer(Customer.fromEntity(entity.getReceiver()))
			.zipcode(entity.getZipcode())
			.address(entity.getAddress())
			.detailAddress(entity.getDetailAddress())
			.build();
	}

}
