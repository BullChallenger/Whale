package com.example.whale.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.Like.entity.LikeEntity;
import com.example.whale.domain.article.entity.ArticleEntity;
import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.global.constant.Role;

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
@Table(indexes = @Index(name = "idx_user_id", columnList = "user_id"))
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String nickname;

    private String password;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<ArticleEntity> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<LikeEntity> hearts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public UserEntity(String email, String username, String nickname, String password, Role role) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public static UserEntity of(String email, String username, String nickname, String password, Role role) {
        return UserEntity.builder()
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(password)
                .role(role)
                .build();
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void writeArticle(ArticleEntity article) {
        this.articles.add(article);
    }

}
