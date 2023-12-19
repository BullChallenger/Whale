package com.example.whale.controller.util;

import com.example.whale.dto.user.AuthenticationUser;
import org.springframework.security.core.Authentication;

public class AuthenticationUtil {

    public static AuthenticationUser convertAuthentication(Authentication authentication) {
        return (AuthenticationUser) authentication.getPrincipal();
    }

}
