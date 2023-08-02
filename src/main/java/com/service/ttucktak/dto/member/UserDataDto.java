package com.service.ttucktak.dto.member;

import com.service.ttucktak.base.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDataDto {
    @Schema(name = "userName", example = "test_user", requiredProperties = "true", description = "유저 네임")
    private String userName;
    @Schema(name = "email", example = "test@email.com", requiredProperties = "true", description = "유저 이메일")
    private String email;
    @Schema(name = "profileImgUrl", example = "http://test_img", requiredProperties = "true", description = "유저 프로필 이미지 url")
    private String profileImgUrl;
    @Schema(name = "accountType", example = "test_type", requiredProperties = "true", description = "유저 타입")
    private AccountType accountType;

    @Builder
    public UserDataDto(String userName, String email, String birthday, String profileImgUrl, AccountType accountType){
        this.userName = userName;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.accountType = accountType;
    }
}
