package com.example.whale.util;

import com.example.whale.constant.Role;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class RoleUtil {

    public Set<GrantedAuthority> addAuthorities(Role userRole) {
        String role = userRole.name();

        Set<GrantedAuthority> authorities = new HashSet<>();

        Assert.isTrue(!role.startsWith("ROLE_"),
                () -> role + " cannot start with ROLE_ (it is automatically added)");
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return authorities;
    }

}
