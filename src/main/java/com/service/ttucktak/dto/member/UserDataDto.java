package com.service.ttucktak.dto.member;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "accountType", example = "test_type", requiredProperties = "true", description = "유저 타")
    private AccountType accountType;

    public UserDataDto(Member member) {
        this.userName = member.getUsername();
        this.email = member.getUserId();
        this.profileImgUrl = member.getProfileImgUrl();
        this.accountType = member.getAccountType();
    }
}
