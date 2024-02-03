package com.example.whale.repository.querydsl;

import com.example.whale.domain.ArticleEntity;
import com.example.whale.dto.article.GetArticlePageResponseDTO;
import com.example.whale.dto.article.GetArticleResponseDTO;
import com.example.whale.dto.article.QGetArticlePageResponseDTO;
import com.example.whale.dto.article.QGetArticleResponseDTO;
import com.example.whale.dto.attachment.GetAttachmentResponseDTO;
import com.example.whale.dto.attachment.QGetAttachmentResponseDTO;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.dto.comment.QGetCommentResponseDTO;
import com.example.whale.dto.user.QWriterResponseDTO;
import com.querydsl.core.Tuple;
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

import static com.example.whale.domain.QArticleEntity.articleEntity;
import static com.example.whale.domain.QAttachmentEntity.attachmentEntity;
import static com.example.whale.domain.QCommentEntity.commentEntity;
import static com.example.whale.domain.QHeartEntity.heartEntity;
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
                    articleEntity.content,
                    Expressions.numberTemplate(Integer.class, "count({0})", heartEntity.id)
                )
        ).from(articleEntity)
         .innerJoin(articleEntity.writer, userEntity)
         .leftJoin(articleEntity.hearts, heartEntity)
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
                        Expressions.numberTemplate(Integer.class, "count({0})", commentEntity.id),
                        Expressions.numberTemplate(Integer.class, "count({0})", heartEntity.id)
                )
        ).from(articleEntity)
         .where(ltArticleId(lastArticleId))
         .innerJoin(articleEntity.writer, userEntity)
         .leftJoin(articleEntity.comments, commentEntity)
         .leftJoin(articleEntity.hearts, heartEntity)
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
        return queryFactory.from(articleEntity).where(articleEntity.id.eq(articleId)).fetchOne() != null;
    }

}
