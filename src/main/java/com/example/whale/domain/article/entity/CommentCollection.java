package com.example.whale.domain.article.entity;

import com.example.whale.domain.comment.entity.CommentEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCollection {

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
    private List<CommentEntity> comments = new ArrayList<>();

    public void addCommentInArticle(CommentEntity commentEntity) {
        this.comments.add(commentEntity);
    }

}
