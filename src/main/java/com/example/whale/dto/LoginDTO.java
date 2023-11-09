package com.example.whale.dto;

import com.example.whale.domain.UserEntity;
import lombok.Getter;

public class LoginDTO {

    @Getter
    public static class LoginRequestDTO {
        private String email;
        private String password;
    }

    @Getter
    public static class LoginResponseDTO {
        private Long id;
        private String email;
        private String username;
        private String nickname;

        public LoginResponseDTO(Long id, String email, String username, String nickname) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.nickname = nickname;
        }

        public static LoginResponseDTO from(UserEntity userEntity) {
            return new LoginResponseDTO(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getUsername(),
                    userEntity.getNickname()
            );
        }
    }

}
