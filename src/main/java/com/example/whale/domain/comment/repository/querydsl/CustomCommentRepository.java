package com.example.whale.domain.comment.repository.querydsl;

import static com.example.whale.domain.comment.entity.QCommentEntity.*;
import static com.example.whale.domain.user.entity.QUserEntity.*;

import org.springframework.stereotype.Repository;

import com.example.whale.domain.comment.dto.GetCommentResponseDTO;
import com.example.whale.domain.comment.dto.GetOneCommentDTO;
import com.example.whale.domain.comment.dto.QGetCommentResponseDTO;
import com.example.whale.domain.comment.dto.QGetOneCommentDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    public GetCommentResponseDTO findCommentById(Long commentId) {
        return queryFactory.select(
                        new QGetCommentResponseDTO(
                                commentEntity.id,
                                userEntity.nickname,
                                commentEntity.content,
                                commentEntity.depth,
                                commentEntity.parentComment.id
                        )
                ).from(commentEntity)
                .innerJoin(commentEntity.writer, userEntity)
                .where(commentEntity.id.eq(commentId))
                .fetchOne();
    }

    public GetOneCommentDTO findOneCommentById(Long commentId) {
        return queryFactory.select(
                new QGetOneCommentDTO(
                        commentEntity.id,
                        userEntity.nickname,
                        commentEntity.content,
                        commentEntity.depth
                )
        ).from(commentEntity)
         .innerJoin(commentEntity.writer, userEntity)
         .where(commentEntity.id.eq(commentId))
         .fetchOne();
    }

    public void deleteCommentById(Long commentId) {
        queryFactory.update(commentEntity)
                     .set(commentEntity.isDeleted, true)
                     .where(commentEntity.id.eq(commentId))
                     .execute();

        queryFactory.update(commentEntity)
                    .set(commentEntity.isDeleted, true)
                    .where(commentEntity.parentComment.id.eq(commentId))
                    .execute();
    }

}
