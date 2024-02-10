package com.example.whale.domain.article.entity;

import com.example.whale.domain.Like.entity.LikeEntity;
import com.example.whale.domain.attachment.entity.AttachmentEntity;
import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.domain.comment.entity.CommentEntity;
import com.example.whale.domain.user.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class ArticleEntity extends BaseEntity {

    @Id
    @Column(name = "ARTICLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID")
    private UserEntity writer;

    @Column(name = "ARTICLE_TITLE")
    private String title;

    @Lob
    @Column(name = "ARTICLE_CONTENT")
    private String content;

    @Embedded
    private AttachmentCollection attachments = new AttachmentCollection();

    @Embedded
    private CommentCollection comments = new CommentCollection();

    @Builder
    public ArticleEntity(UserEntity writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public static final ArticleEntity of(UserEntity writer, String title, String content) {
        return ArticleEntity.builder()
                            .writer(writer)
                            .title(title)
                            .content(content)
                            .build();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
