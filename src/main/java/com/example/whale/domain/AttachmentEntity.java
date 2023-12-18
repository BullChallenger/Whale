package com.example.whale.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private ArticleEntity article;

    private String fileNameInStorage;

    private String fileOriginName;

    private String fileUrl;

    private Long fileSize;

    @Builder
    public AttachmentEntity(ArticleEntity article, String fileNameInStore, String fileOriginName, String fileUrl, Long fileSize) {
        this.article = article;
        this.fileNameInStorage = fileNameInStore;
        this.fileOriginName = fileOriginName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
    }

    public static AttachmentEntity of(ArticleEntity article, String fileNameInStore, String fileOriginName, String fileUrl, Long fileSize) {
        return AttachmentEntity.builder()
                                .article(article)
                                .fileNameInStore(fileNameInStore)
                                .fileOriginName(fileOriginName)
                                .fileUrl(fileUrl)
                                .fileSize(fileSize)
                                .build();
    }

}
