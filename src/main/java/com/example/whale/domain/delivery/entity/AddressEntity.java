package com.example.whale.domain.delivery.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.example.whale.domain.user.entity.UserEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private UserEntity user;

	private String zipcode; // 우편번호
	private String address; // 거주지
	private String detailAddress; // 상세 주소

	@Builder
	public AddressEntity(UserEntity user, String zipcode, String address, String detailAddress) {
		this.user = user;
		this.zipcode = zipcode;
		this.address = address;
		this.detailAddress = detailAddress;
	}

	public static AddressEntity of(UserEntity user, String zipcode, String address, String detailAddress) {
		return AddressEntity.builder()
			.user(user)
			.zipcode(zipcode)
			.address(address)
			.detailAddress(detailAddress)
			.build();
	}

	public void updateZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public void updateAddress(String address) {
		this.address = address;
	}

	public void updateDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

}
