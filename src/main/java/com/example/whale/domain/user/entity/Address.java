package com.example.whale.domain.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.whale.domain.common.entity.PersistableWrapper;

import lombok.Getter;

@Getter
@Entity
public class Address extends PersistableWrapper {

	@Id
	private String id; // {

	private String zipcode; // 우편번호
	private String address; // 거주지
	private String detailAddress; // 상세 주소

}
