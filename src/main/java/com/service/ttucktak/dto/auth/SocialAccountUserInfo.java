package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SocialAccountUserInfo {
    @Schema(name = "userName", example = "test_user", requiredProperties = "true", description = "유저 네임")
    private final String userName;
    @Schema(name = "userEmail", example = "test@email.com", requiredProperties = "true", description = "유저 닉네임")
    private final String userEmail;
    @Schema(name = "imgURL", example = "user IMG URL", requiredProperties = "true", description = "유저 이미지 url 링크")
    private final String imgURL;
}