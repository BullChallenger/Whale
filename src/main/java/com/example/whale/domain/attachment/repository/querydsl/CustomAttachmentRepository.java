package com.example.whale.domain.attachment.repository.querydsl;

import static com.example.whale.domain.attachment.entity.QAttachmentEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomAttachmentRepository {

    private final JPAQueryFactory queryFactory;

    public void deleteAttachmentInArticleById(String attachmentId){
        queryFactory.update(attachmentEntity)
                    .set(attachmentEntity.isDeleted, true)
                    .where(attachmentEntity.id.eq(attachmentId))
                    .execute();
    }

}
