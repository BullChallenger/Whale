package com.example.whale.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class WriterResponseDTO {

    private final Long writerId;
    private final String writerNickname;

    @Builder
    @QueryProjection
    public WriterResponseDTO(Long writerId, String writerNickname) {
        this.writerId = writerId;
        this.writerNickname = writerNickname;
    }

    public static WriterResponseDTO of(Long writerId, String writerNickname) {
        return WriterResponseDTO.builder()
                                .writerId(writerId)
                                .writerNickname(writerNickname)
                                .build();
    }

}
