package com.service.ttucktak.config;

import com.service.ttucktak.dto.auth.OAuthAttribute;
import com.service.ttucktak.dto.auth.SessionUser;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



import java.text.ParseException;

/**
 * OAuth User 정보 서비스 &
 * 실제 서비스시 삭제
 * @author LEE JIHO
 * @needToDelete true
 * */
@RequiredArgsConstructor
@Service
@Lazy
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;

    //OAuth Request 해서 가져온 유저 정보 로딩
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 소셜 로그인 서비스 운영사 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth 로그인 시에 키로 사용하는 값
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttribute attribute = OAuthAttribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member users = saveOrUpdate(attribute);

        httpSession.setAttribute("user", new SessionUser(users));

        return oAuth2User;
    }

    private Member saveOrUpdate(OAuthAttribute attribute){
        Member users = null;
        
        try{
            users = memberRepository.findByUserId(attribute.getUserEmail())
                    .map(entity -> {
                        try {
                            return entity.update(attribute.getUserName(), attribute.getUserEmail());
                        } catch (ParseException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    })
                    .orElse(attribute.toEntity());
        }catch (Exception exception){
            logger.error("Error in CustomOAuthUserService : " + exception.getMessage());
        }
        
        return users;
    }
}
