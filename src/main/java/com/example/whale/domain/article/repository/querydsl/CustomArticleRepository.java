package com.example.whale.domain.article.repository.querydsl;

import static com.example.whale.domain.article.entity.QArticleEntity.*;
import static com.example.whale.domain.attachment.entity.QAttachmentEntity.*;
import static com.example.whale.domain.comment.entity.QCommentEntity.*;
import static com.example.whale.domain.user.entity.QUserEntity.*;

import com.example.whale.domain.article.dto.GetArticleResponseConvertDTO;
import com.example.whale.domain.article.dto.GetArticleResponseDTOV2;
import com.example.whale.domain.article.dto.QGetArticleResponseDTOV2;
import com.example.whale.domain.article.entity.QArticleEntity;
import com.example.whale.domain.attachment.entity.QAttachmentEntity;
import com.example.whale.domain.comment.entity.QCommentEntity;
import com.example.whale.domain.user.entity.QUserEntity;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

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

@Repository
@RequiredArgsConstructor
public class CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

    public GetArticleResponseConvertDTO readArticleByIdV2(Long articleId) {
        QArticleEntity article = QArticleEntity.articleEntity;
        QUserEntity a_writer = QUserEntity.userEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        QUserEntity c_writer = new QUserEntity("c_writer");  // 별칭을 직접 지정
        QAttachmentEntity attachment = QAttachmentEntity.attachmentEntity;

        List<GetArticleResponseDTOV2> duplicatedArticleInfo = queryFactory
                .selectDistinct(
                        new QGetArticleResponseDTOV2(
                                article.id,
                                a_writer.nickname,
                                article.title,
                                article.content,
                                comment.id,
                                c_writer.nickname,
                                comment.content,
                                comment.depth,
                                comment.parentComment.id,
                                attachment.id,
                                attachment.fileOriginName,
                                attachment.filePath,
                                attachment.fileExtension,
                                attachment.contentType
                        )
                )
                .from(article)
                .innerJoin(a_writer).on(article.writer.id.eq(a_writer.id))
                .leftJoin(comment).on(article.id.eq(comment.article.id))
                .leftJoin(c_writer).on(comment.article.writer.id.eq(c_writer.id))
                .leftJoin(attachment).on(article.id.eq(attachment.article.id))
                .where(article.id.eq(articleId))
                .fetch();

        GetArticleResponseConvertDTO dto = GetArticleResponseConvertDTO.from(duplicatedArticleInfo.get(0));
        for (GetArticleResponseDTOV2 origin : duplicatedArticleInfo) {
            dto.convert(origin);
        }

        return dto;
    }

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
