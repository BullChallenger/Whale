package com.example.whale.fixture;

import com.example.whale.domain.user.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.global.constant.Role;

public class UserFixture {

    public static UserEntity getDefaultUserEntityFixture() {
        return UserEntity.of(
                "dummy@dummy.com",
                "john doe",
                "dummy",
                "{noop}1234",
                Role.USER
        );
    }

    public static SignUpRequestDTO returnSignUpRequestDTO() {
        return new SignUpRequestDTO(
                "dummy@dummy.com",
                "john doe",
                "dummy",
                "1234"
        );
    }

}
