package com.example.whale.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.whale.global.security.filter.JwtAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
public class JwtAuthenticationFilterConfigForIgnore {

        @Bean
        public FilterRegistrationBean<JwtAuthenticationFilter> registration(JwtAuthenticationFilter filter) {
            FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
            registration.setEnabled(false);
            return registration;
        }

}
