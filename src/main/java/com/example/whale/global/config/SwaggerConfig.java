package com.example.whale.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.whale.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .securitySchemes(Arrays.asList(apiKeyHeader(), apiKeyCookie()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey apiKeyHeader() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiKey apiKeyCookie() {
        return new ApiKey("Set-Cookie", "Set-Cookie", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(headerAuthReference(), cookieAuthReference()))
                .build();
    }

    private SecurityReference headerAuthReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return new SecurityReference("Authorization", authorizationScopes);
    }

    private SecurityReference cookieAuthReference() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return new SecurityReference("Authorization-refresh", authorizationScopes);
    }
}
