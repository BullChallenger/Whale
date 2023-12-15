package com.example.whale.domain;

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

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<CommentEntity> comments = new ArrayList<>();

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

    public void addCommentInArticle(CommentEntity commentEntity) {
        this.comments.add(commentEntity);
    }
    
}
