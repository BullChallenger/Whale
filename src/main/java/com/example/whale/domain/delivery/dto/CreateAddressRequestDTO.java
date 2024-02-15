package com.example.whale.domain.delivery.dto;

import lombok.Getter;

@Getter
public class CreateAddressRequestDTO {

	private final Long userId;
	private final String zipcode;
	private final String address;
	private final String detailAddress;

	public CreateAddressRequestDTO(Long userId, String zipcode, String address, String detailAddress) {
		this.userId = userId;
		this.zipcode = zipcode;
		this.address = address;
		this.detailAddress = detailAddress;
	}

}
