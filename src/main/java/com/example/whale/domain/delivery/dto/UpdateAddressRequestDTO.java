package com.example.whale.domain.delivery.dto;

import lombok.Getter;

@Getter
public class UpdateAddressRequestDTO {

	private final Long addressId;

	private final String zipcode;
	private final String address;
	private final String detailAddress;

	public UpdateAddressRequestDTO(Long addressId, String zipcode, String address, String detailAddress) {
		this.addressId = addressId;
		this.zipcode = zipcode;
		this.address = address;
		this.detailAddress = detailAddress;
	}

}
