package com.example.whale.domain.user.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import com.example.whale.domain.common.entity.BaseEntity;
import com.example.whale.global.constant.Role;
import com.example.whale.global.util.converter.RoleListConverter;

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

    @Convert(converter = RoleListConverter.class)
    private List<Role> roles;

    @Builder
    public UserEntity(String email, String username, String nickname, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.roles = roles;
    }

    public static UserEntity of(String email, String username, String nickname, String password, List<Role> roles) {
        return UserEntity.builder()
                .email(email)
                .username(username)
                .nickname(nickname)
                .password(password)
                .roles(roles)
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

}
