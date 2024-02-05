package com.example.whale.domain.article.repository.querydsl;

import static com.example.whale.domain.article.entity.QArticleEntity.articleEntity;
import static com.example.whale.domain.attachment.entity.QAttachmentEntity.attachmentEntity;
import static com.example.whale.domain.comment.entity.QCommentEntity.commentEntity;
import static com.example.whale.domain.user.entity.QUserEntity.userEntity;

import com.example.whale.domain.article.dto.GetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTO;
import com.example.whale.domain.article.dto.QGetArticlePageResponseDTO;
import com.example.whale.domain.article.dto.QGetArticleResponseDTO;
import com.example.whale.domain.attachment.dto.GetAttachmentResponseDTO;
import com.example.whale.domain.attachment.dto.QGetAttachmentResponseDTO;
import com.example.whale.domain.comment.dto.GetCommentResponseDTO;

import com.example.whale.domain.comment.dto.QGetCommentResponseDTO;
import com.example.whale.domain.user.dto.QWriterResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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

        List<GetAttachmentResponseDTO> attachmentsInArticle = queryFactory.select(
                new QGetAttachmentResponseDTO(
                        attachmentEntity.id,
                        attachmentEntity.fileOriginName,
                        attachmentEntity.filePath,
                        attachmentEntity.fileExtension,
                        attachmentEntity.contentType
                )
        ).from(attachmentEntity)
         .innerJoin(attachmentEntity.article, articleEntity)
         .where(attachmentEntity.article.id.eq(articleId))
         .fetch();

        assert articleResponse != null;
        articleResponse.setCommentsInArticle(commentsInArticle);
        articleResponse.setAttachmentInArticle(attachmentsInArticle);

        return articleResponse;
    }

    public void deleteArticleById(Long articleId) {
        queryFactory.update(articleEntity)
                .set(articleEntity.isDeleted, true)
                .where(articleEntity.id.eq(articleId))
                .execute();

        queryFactory.update(commentEntity)
                .set(commentEntity.isDeleted, true)
                .where(commentEntity.article.id.eq(articleId))
                .execute();

        queryFactory.update(attachmentEntity)
                .set(attachmentEntity.isDeleted, true)
                .where(attachmentEntity.article.id.eq(articleId))
                .execute();
    }

    public Page<GetArticlePageResponseDTO> readArticlePage(Long lastArticleId, Pageable pageable) {
        List<GetArticlePageResponseDTO> pageContent = queryFactory.select(
                new QGetArticlePageResponseDTO(
                        articleEntity.id,
                        articleEntity.title,
                        new QWriterResponseDTO(
                            userEntity.id,
                            userEntity.nickname
                        ),
                        Expressions.numberTemplate(Integer.class, "count({0})", commentEntity.id)
                )
        ).from(articleEntity)
         .where(ltArticleId(lastArticleId))
         .innerJoin(articleEntity.writer, userEntity)
         .leftJoin(articleEntity.comments, commentEntity)
         .groupBy(articleEntity.id)
         .orderBy(articleEntity.id.desc())
         .limit(pageable.getPageSize())
         .fetch();

        JPAQuery<Long> countArticleQuery = queryFactory
                                        .select(articleEntity.count())
                                        .from(articleEntity);

        return PageableExecutionUtils.getPage(pageContent, pageable, countArticleQuery::fetchOne);
    }

    private BooleanExpression ltArticleId(Long articleId) {
        if (articleId == null) {
            return null;
        }

        return articleEntity.id.lt(articleId);
    }

    public boolean isArticleExists(Long articleId) {
        return queryFactory
                .selectOne()
                .from(articleEntity)
                .where(articleEntity.id.eq(articleId))
                .fetchFirst() != null;
    }

}
