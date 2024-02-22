package com.example.whale.domain.delivery.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressInOrderDTO {

	private final Long id;

	private final String zipcode; // 우편번호
	private final String address; // 거주지
	private final String detailAddress; // 상세 주소

	@Builder
	@QueryProjection
	public AddressInOrderDTO(Long id, String zipcode, String address, String detailAddress) {
		this.id = id;
		this.zipcode = zipcode;
		this.address = address;
		this.detailAddress = detailAddress;
	}

}
