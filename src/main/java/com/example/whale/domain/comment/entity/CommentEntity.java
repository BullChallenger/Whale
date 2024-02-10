package com.example.whale.domain.comment.entity;

import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.domain.user.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_article_id", columnList = "article_id"))
public class CommentEntity extends BaseEntity {

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "WRITER_ID")
    private UserEntity writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private int depth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_COMMENT_ID")
    private CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<CommentEntity> childComments = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private ArticleEntity article;

    @Builder
    public CommentEntity(UserEntity writer, String content, CommentEntity parentComment, ArticleEntity article) {
        this.writer = writer;
        this.content = content;
        this.parentComment = parentComment;
        this.depth = setDepth(parentComment);
        this.article = article;
    }

    public static CommentEntity of(UserEntity writer, String content, CommentEntity parentComment, ArticleEntity article) {
        return CommentEntity.builder()
                            .writer(writer)
                            .content(content)
                            .parentComment(parentComment)
                            .article(article)
                            .build();
    }

    private int setDepth(CommentEntity parentComment) {
        if (!hasParentComment(parentComment)) {
            return 0;
        }
        return calculateThisCommentDepth(parentComment.getDepth());
    }

    private boolean hasParentComment(CommentEntity parentComment) {
        return parentComment != null;
    }

    private int calculateThisCommentDepth(int parentCommentDepth) {
        return parentCommentDepth + 1;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
