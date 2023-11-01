package com.example.whale.domain;

import com.example.whale.constant.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Entity
@Getter
@DynamicUpdate
@Where(clause = "IS_DELETED = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault(value = "false")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Builder
    public User(String email, String username, String nickname, String password, Role role) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    public static final User of(String email, String username, String nickname, String password, Role role) {
        return User.builder()
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(password)
                .role(role)
                .build();
    }

    public void updateIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

}
