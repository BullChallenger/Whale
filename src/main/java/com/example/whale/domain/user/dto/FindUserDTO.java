package com.example.whale.domain.user.dto;

import com.example.whale.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

public class FindUserDTO {

    @Getter
    public static class FindUserResponseDTO {
        private final String email;
        private final String username;
        private final String nickname;

        @Builder
        public FindUserResponseDTO(String email, String username, String nickname) {
            this.email = email;
            this.username = username;
            this.nickname = nickname;
        }

        public static FindUserResponseDTO from(UserEntity userEntity) {
            return FindUserResponseDTO.builder()
                                        .email(userEntity.getEmail())
                                        .username(userEntity.getUsername())
                                        .nickname(userEntity.getNickname())
                                        .build();
        }
    }

}
