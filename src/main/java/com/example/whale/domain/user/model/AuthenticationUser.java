package com.example.whale.domain.user.model;

import com.example.whale.domain.user.entity.UserEntity;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationUser implements UserDetails, Serializable {

    @Getter
    private Long id;

    @Getter
    private String email;

    @Getter
    private String nickname;

    private String username;

    private String password;

    private Set<GrantedAuthorityImpl> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @Builder
    public AuthenticationUser(Long id, String email, String nickname, String username, String password,
                              Set<GrantedAuthorityImpl> authorities, boolean accountNonExpired, boolean accountNonLocked,
                              boolean credentialsNonExpired, boolean enabled) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static AuthenticationUser of(UserEntity entity, Set<GrantedAuthorityImpl> authorities) {
        return AuthenticationUser.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .authorities(authorities)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }


    public static AuthenticationUser of(Long id, String email, String nickname, String username, String password,
                                        Set<GrantedAuthorityImpl> authorities) {
        return AuthenticationUser.builder()
                                    .id(id)
                                    .email(email)
                                    .nickname(nickname)
                                    .username(username)
                                    .password(password)
                                    .authorities(authorities)
                                    .accountNonExpired(true)
                                    .accountNonLocked(true)
                                    .credentialsNonExpired(true)
                                    .enabled(true)
                                    .build();
    }

    public static AuthenticationUser of(Long id, String email, String nickname, String username, String password, Set<GrantedAuthorityImpl> authorities,
                                        boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        return AuthenticationUser.builder()
                                    .id(id)
                                    .email(email)
                                    .nickname(nickname)
                                    .username(username)
                                    .password(password)
                                    .authorities(authorities)
                                    .accountNonExpired(accountNonExpired)
                                    .accountNonLocked(accountNonLocked)
                                    .credentialsNonExpired(credentialsNonExpired)
                                    .enabled(enabled)
                                    .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
