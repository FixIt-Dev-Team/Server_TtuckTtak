package com.service.ttucktak.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SocialAccountUserInfo {
    private final String userName;
    private final String userEmail;
    private final String imgURL;
}