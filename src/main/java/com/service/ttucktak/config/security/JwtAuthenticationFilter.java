package com.service.ttucktak.config.security;

import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //token 추출
        String token = extractToken((HttpServletRequest) request);

        //토큰 유효성 검사
        if(token != null && jwtUtil.checkToken(token)){
            try {
                //Authentication 객체를 가지고와서 SecurityContext 에 추가
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (BaseException exception) {
                log.error(exception.getMessage());
                throw new RuntimeException(exception);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Request 에서 토큰 추출
     * @return AccessToken
     * */
    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader(CustomHttpHeaders.AUTHORIZATION);
        String[] parsedToken = bearerToken.split(" ");

        if(parsedToken[0].equalsIgnoreCase("Bearer")){
            return parsedToken[1];
        }

        return null;
    }
}
