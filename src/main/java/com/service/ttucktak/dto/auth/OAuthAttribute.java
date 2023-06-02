package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.Role;
import com.service.ttucktak.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public UserEntity toEntity() throws ParseException {
        String year = "1998";
        String month = birthday.substring(0, 2);
        String day = birthday.substring(2);
        this.birthday = year + "-" + month + "-" + day;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(birthday);

        return UserEntity.builder()
                .userID(userEmail)
                .userPW("kakao")
                .userName(userName)
                .email(userEmail)
                .birthday(date)
                .validation(false)
                .accountType(accountType)
                .role(Role.ROLE_USER)
                .build();
    }
}
