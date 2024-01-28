package com.example.whale.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.whale.domain.QAttachmentEntity.attachmentEntity;

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
