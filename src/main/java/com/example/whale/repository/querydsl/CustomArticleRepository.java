package com.example.whale.repository.querydsl;

import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.QGetArticleResponseDTO;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.dto.comment.QGetCommentResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.whale.domain.QArticleEntity.articleEntity;
import static com.example.whale.domain.QCommentEntity.commentEntity;
import static com.example.whale.domain.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

    public GetArticleResponseDTO readArticleById(Long articleId) {
        GetArticleResponseDTO articleResponse =  queryFactory.select(
                new QGetArticleResponseDTO(
                    articleEntity.id,
                    userEntity.nickname,
                    articleEntity.title,
                    articleEntity.content
                )
        ).from(articleEntity)
         .innerJoin(articleEntity.writer, userEntity)
         .where(articleEntity.id.eq(articleId))
         .fetchOne();

        List<GetCommentResponseDTO> commentsInArticle = queryFactory.select(
                new QGetCommentResponseDTO(
                        commentEntity.id,
                        userEntity.nickname,
                        commentEntity.content,
                        commentEntity.depth,
                        commentEntity.parentComment.id
                )
        ).from(commentEntity)
         .innerJoin(commentEntity.writer, userEntity)
         .where(commentEntity.article.id.eq(articleId))
         .fetch();

        assert articleResponse != null;
        articleResponse.setCommentsInArticle(commentsInArticle);

        return articleResponse;
    }

}
