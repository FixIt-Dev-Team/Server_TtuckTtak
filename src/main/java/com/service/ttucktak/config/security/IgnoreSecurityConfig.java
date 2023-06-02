package com.service.ttucktak.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
@Configuration
public class IgnoreSecurityConfig {

    @Order(0)
    @Bean
    public SecurityFilterChain IgnoreFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 토큰으로 인증하므로 stateless
                .and()
                .headers().frameOptions().disable()
                .and()
                .formLogin().disable()//Form Based Authentication 을 사용하지 않음
                .httpBasic().disable()// HTTP Basic Authentication 을 사용하지 않음
                .authorizeHttpRequests() //Http Request를 인가하라
                .requestMatchers("/api/auths/signup").permitAll()
                .requestMatchers("/api/auths/login").permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }
}
