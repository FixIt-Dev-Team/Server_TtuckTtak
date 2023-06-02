package com.service.ttucktak.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    private final CustomOAuthUserService customOAuthUserService;

    @Autowired
    public SecurityConfig(CustomOAuthUserService customOAuthUserService) {
        this.customOAuthUserService = customOAuthUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        http.csrf().disable()//csrf 토큰 비활성화
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests() //Http Request를 인가하라
                .requestMatchers("/api/auths/**").permitAll()// 이쪽은 그냥 허용
                .requestMatchers("/api/users/**").authenticated()// 이쪽은 인증된 유저만
                .requestMatchers("/api/admins/**").hasRole("ROLE_ADMIN") //이쪽은 ADMIN만 접근이 가능하다.
                .anyRequest().denyAll() //위 요청을 제외한 모든 요청은 거절하라
                .and()//그리고
                .oauth2Login() //OAuth Login은 다음과 같다
                .userInfoEndpoint()
                .userService(customOAuthUserService);

        //세션 갯수 제한 -> 1개 + 중복 로그인 시 기존 로그인 유지
        http.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false);

        //로그아웃은 누구나
        http.logout().permitAll();

        return http.build();
    }
}
