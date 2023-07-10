package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.text.ParseException;
import java.util.Map;

/**
 * OAuth로 가져온 유저 정보를 저장하는 class
 * */
@Getter
public class OAuthAttribute {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String userName;
    private final String userEmail;
    private String birthday;
    private static int accountType;

    @Builder
    public OAuthAttribute(Map<String, Object> attributes, String nameAttributeKey, String userName, String userEmail, String birthday) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.userName = userName;
        this.userEmail = userEmail;
        this.birthday = birthday;
    }

    public static OAuthAttribute of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttribute ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        accountType = 1;

        return OAuthAttribute.builder()
                .userName((String) kakaoAccount.get("name"))
                .userEmail((String) kakaoAccount.get("email"))
                .birthday((String) kakaoAccount.get("birthday"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() throws ParseException {
        AccountType type;

        if(accountType == 0) type = AccountType.DEFAULT;
        else if(accountType == 1) type = AccountType.KAKAO;
        else type = AccountType.GOOGLE;

        return Member.builder()
                .userId(userEmail)
                .userPw("kakao")
                .nickname(userName)
                .accountType(type)
                .build();
    }
}
