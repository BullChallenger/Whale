package com.example.whale.domain.user.model;

import com.example.whale.domain.user.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Customer {

	private final Long userId;
	private final String email;
	private final String username;
	private final String nickname;

	@Builder
	public Customer(Long userId, String email, String username, String nickname) {
		this.userId = userId;
		this.email = email;
		this.username = username;
		this.nickname = nickname;
	}

	public static Customer fromEntity(UserEntity entity) {
		return Customer.builder()
				.userId(entity.getId())
				.email(entity.getEmail())
				.username(entity.getUsername())
				.nickname(entity.getNickname())
				.build();
	}

}
