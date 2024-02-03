package com.example.whale.global.util;

import com.example.whale.domain.user.model.AuthenticationUser;
import org.springframework.security.core.Authentication;

public class AuthenticationUtil {

    public static AuthenticationUser convertAuthentication(Authentication authentication) {
        return (AuthenticationUser) authentication.getPrincipal();
    }

}
