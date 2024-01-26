package com.example.whale.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_in_article_id", columnList = "article_id"))
public class AttachmentEntity extends BaseEntity {

    @Id
    @Column(name = "ATTACHMENT_ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private ArticleEntity article;

    private String fileOriginName;

    private String filePath;

    private String fileExtension;

    private String contentType;

    private Long fileSize;

    @Builder
    public AttachmentEntity(String id,
        ArticleEntity article,
        String fileOriginName,
        String filePath,
        String fileExtension,
        String contentType,
        Long fileSize
    ) {
        this.article = article;
        this.fileOriginName = fileOriginName;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public static AttachmentEntity of(String id,
        ArticleEntity article,
        String fileOriginName,
        String filePath,
        String fileExtension,
        String contentType,
        Long fileSize) {
        return AttachmentEntity.builder()
                                .id(id)
                                .article(article)
                                .fileOriginName(fileOriginName)
                                .filePath(filePath)
                                .fileExtension(fileExtension)
                                .contentType(contentType)
                                .fileSize(fileSize)
                                .build();
    }

}
