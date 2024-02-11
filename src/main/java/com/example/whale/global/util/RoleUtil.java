package com.example.whale.global.util;

import com.example.whale.global.constant.Role;
import com.example.whale.domain.user.model.GrantedAuthorityImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RoleUtil {

    public Set<GrantedAuthorityImpl> addAuthorities(Role userRole) {
        String role = userRole.name();

        Set<GrantedAuthorityImpl> authorities = new HashSet<>();

        Assert.isTrue(!role.startsWith("ROLE_"),
                () -> role + " cannot start with ROLE_ (it is automatically added)");
        authorities.add(new GrantedAuthorityImpl("ROLE_" + role));

        return authorities;
    }

    public Set<GrantedAuthorityImpl> addAllAuthorities(List<Role> userRole) {
        List<String> userRoles = userRole.stream().map(role -> role.getAuthority()).toList();

        Set<GrantedAuthorityImpl> authorities = new HashSet<>();

        List<GrantedAuthorityImpl> userAuthorities = userRoles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(role -> new GrantedAuthorityImpl(role))
                .toList();

        authorities.addAll(userAuthorities);

        return authorities;
    }

}
