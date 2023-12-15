package com.example.whale.dto.user;

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
