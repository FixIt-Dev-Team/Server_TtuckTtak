package com.service.ttucktak.utils;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.config.security.CustomHttpHeaders;
import com.service.ttucktak.dto.auth.TokensDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String jwtKey;

    /**
     * create accessToken and refreshToken
     * @param authentication
     * @return TokensDto
     * */
    public TokensDto createTokens(Authentication authentication){
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        long validLength = 1000L * 60 * 60 * 24 * 7;
        Date expireDate = new Date(validLength);

        String accessToken =  Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new TokensDto("Bearer", accessToken, refreshToken);
    }


    /**
     * Extract user info from access token
     * @param token
     * @return UsernamePasswordAuthenticationToken
     * */
    public Authentication getAuthentication(String token) throws BaseException {
        Jws<Claims> claimsJws;

        try{
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtKey).build()
                    .parseClaimsJws(token);

        } catch(Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.INVALID_JWT_TOKEN);
        }

        // 인증 정보 받아오기
        Collection<? extends GrantedAuthority> authorities =
                //Jwt token 에서 auth 필드에대한 스트림을 열어서 ,로 스플릿 한다음에 SimpleGrantedAuthority 로 매핑해서 리스트로 반환
                Arrays.stream(claimsJws.getBody().get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new) .toList();

        UserDetails principal = new User(claimsJws.getBody().getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * Check access token valid
     * @param token
     * @return boolean (true: valid, false: invalid)
     * */
    public boolean checkToken(String token){
        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception exception){
            log.info("invalid JWT Token" + exception.getMessage());
        }

        return false;
    }

}
