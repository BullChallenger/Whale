package com.example.whale.domain.user.dto;

import lombok.Getter;

public class SignUpDTO {

    @Getter
    public static class SignUpRequestDTO {
        private String email;
        private String username;
        private String nickname;
        private String password;
    }

}
