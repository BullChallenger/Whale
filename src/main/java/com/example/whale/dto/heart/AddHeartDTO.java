package com.example.whale.dto.heart;

import com.example.whale.domain.HeartEntity;
import lombok.Getter;

public class AddHeartDTO {

    @Getter
    public static class AddHeartRequestDTO {
        private Long userId;
        private Long articleId;
    }

    @Getter
    public static class AddHeartResponseDTO {
        private Long heartId;

        public AddHeartResponseDTO(HeartEntity savedHeart) {
            this.heartId = savedHeart.getId();
        }
    }

}
