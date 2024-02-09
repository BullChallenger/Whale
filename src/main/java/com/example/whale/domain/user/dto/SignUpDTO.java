package com.example.whale.domain.user.dto;

import lombok.Getter;

public class SignUpDTO {

    @Getter
    public static class SignUpRequestDTO {
        private final String email;
        private final String username;
        private final String nickname;
        private final String password;

        public SignUpRequestDTO(String email, String username, String nickname, String password) {
            this.email = email;
            this.username = username;
            this.nickname = nickname;
            this.password = password;
        }
    }

}
