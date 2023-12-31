package com.example.whale.repository.querydsl;

import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.dto.comment.GetOneCommentDTO;
import com.example.whale.dto.comment.QGetCommentResponseDTO;
import com.example.whale.dto.comment.QGetOneCommentDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.whale.domain.QCommentEntity.commentEntity;
import static com.example.whale.domain.QUserEntity.userEntity;

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
