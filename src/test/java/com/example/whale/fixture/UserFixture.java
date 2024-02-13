package com.example.whale.fixture;

import static org.mockito.Mockito.*;

import java.util.List;

import org.mockito.Mockito;

import com.example.whale.domain.user.dto.SignUpDTO.SignUpRequestDTO;
import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.global.constant.Role;

public class UserFixture {

    public static UserEntity getDefaultUserEntityFixture() {
        UserEntity fixture = Mockito.spy(UserEntity.of(
            "dummy@dummy.com",
            "john doe",
            "dummy",
            "{noop}1234",
            List.of(Role.USER)
        ));

        when(fixture.getId()).thenReturn(1L);
        return fixture;
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
