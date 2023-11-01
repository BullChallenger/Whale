package com.example.whale.dto;

import java.util.Collection;
import java.util.Set;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationUser implements UserDetails  {

    private final Long id;

    private final String email;

    private final String nickname;

    private final String username;

    private final String password;

    private final Set<GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    @Builder
    public AuthenticationUser(Long id, String email, String nickname, String username, String password,
                              Set<GrantedAuthority> authorities, boolean accountNonExpired, boolean accountNonLocked,
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

    public static AuthenticationUser of(Long id, String email, String nickname, String username, String password,
                                        Set<GrantedAuthority> authorities) {
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

    public static AuthenticationUser of(Long id, String email, String nickname, String username, String password, Set<GrantedAuthority> authorities,
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
        return this.email;
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
