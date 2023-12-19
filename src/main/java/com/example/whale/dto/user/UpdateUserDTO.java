package com.example.whale.dto.user;

import com.example.whale.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;

public class UpdateUserDTO {

    @Getter
    public static class UpdateUserRequestDTO {
        private String email;
        private String username;
        private String nickname;
    }

    @Getter
    public static class UpdateUserResponseDTO {
        private final Long userId;
        private final String email;
        private final String username;
        private final String nickname;

        @Builder
        public UpdateUserResponseDTO(Long userId, String email, String username, String nickname) {
            this.userId = userId;
            this.email = email;
            this.username = username;
            this.nickname = nickname;
        }

        public static final UpdateUserResponseDTO from(UserEntity userEntity) {
            return UpdateUserResponseDTO.builder()
                                        .userId(userEntity.getId())
                                        .email(userEntity.getEmail())
                                        .username(userEntity.getUsername())
                                        .nickname(userEntity.getNickname())
                                        .build();
        }
    }

}
