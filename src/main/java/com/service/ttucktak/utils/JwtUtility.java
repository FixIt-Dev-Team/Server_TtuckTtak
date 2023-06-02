package com.service.ttucktak.utils;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.TokensDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtUtility {
    @Value("${jwt.secret-key}")
    private String jwtKey;

    /**
     * jwtToken 생성 메서드
     * @param : userIdx(UUID)
     * @return : jwtToken(String)
     * */
    public String createAccessToken(UUID userIdx){

        long validLength = 1000L * 60 * 60 * 24 * 7;
        Date now = new Date();
        Date expireDate = new Date(validLength);

        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();

    }

    /**
     * refresh Token 생성 메서드
     * @param : None
     * @return : refreshToken(String)
     * */
    public String createRefreshToken(){
        long validLength = 1000L * 60 * 60 * 24 * 30;
        Date now = new Date();
        Date expireDate = new Date(validLength);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }

    /**
     * accessToken 재발급 메서드
     * @param : userIdx
     * @return : Token DTO(accessToken + refreshToken)
     * */
    public TokensDto refreshAccessToken(UUID userIdx){
        long accessValidLength = 1000L * 60 * 60 * 24 * 7;
        Date now = new Date();
        Date accessExpireDate = new Date(accessValidLength);

        String accessToken = Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .setExpiration(accessExpireDate)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();

        long refreshValidLength = 1000L * 60 * 60 * 24 * 30;
        Date refreshExpireDate = new Date(refreshValidLength);

        String refreshToken =  Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(refreshExpireDate)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * jwtToken 추출 메서드
     * @param : NULL
     * @return : jwtToken(String)
     * */
    public String getAccessToken(){
        HttpServletRequest request = (HttpServletRequest) (RequestContextHolder.currentRequestAttributes());
        return request.getHeader("SERVER-ACCESS-TOKEN");
    }

    public String getRefreshToken(){
        HttpServletRequest request = (HttpServletRequest) (RequestContextHolder.currentRequestAttributes());
        return request.getHeader("SERVER-REFRESH-TOKEN");
    }

    /**
     * userIdx 추출 메서드
     * @param : jwtToken(String)
     * @return : userIdx(UUID)
     * */
    public UUID getUserIdxFromToken(String token) throws BaseException {
        Jws<Claims> claimsJws;

        try{
            claimsJws = Jwts.parser()
                    .setSigningKey(jwtKey)
                    .parseClaimsJws(token);
        } catch (SignatureException exception){
            throw new BaseException(BaseErrorCode.CORRUPTED_TOKEN);
        } catch (MalformedJwtException exception) {
            throw new BaseException(BaseErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException exception) {
            throw new BaseException(BaseErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException exception){
            throw new BaseException(BaseErrorCode.UNSUPPORTED_TOKEN);
        }

        return claimsJws.getBody().get("userIdx", UUID.class);
    }
}
