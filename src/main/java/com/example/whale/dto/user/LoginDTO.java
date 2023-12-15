package com.example.whale.dto.user;

import lombok.Getter;

public class LoginDTO {

    @Getter
    public static class LoginRequestDTO {
        private String email;
        private String password;
    }

    @Getter
    public static class LoginResponseDTO {
        private final Long id;
        private final String email;
        private final String username;
        private final String nickname;

        public LoginResponseDTO(Long id, String email, String username, String nickname) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.nickname = nickname;
        }

        public static LoginResponseDTO from(AuthenticationUser authUser) {
            return new LoginResponseDTO(
                    authUser.getId(),
                    authUser.getEmail(),
                    authUser.getUsername(),
                    authUser.getNickname()
            );
        }
    }

}
