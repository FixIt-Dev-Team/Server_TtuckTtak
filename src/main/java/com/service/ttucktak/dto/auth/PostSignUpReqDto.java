package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.entity.annotation.Nickname;
import com.service.ttucktak.entity.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSignUpReqDto {
    @Schema(name = "userId", example = "ttukttak@ttukttak.com", requiredProperties = "true", description = "유저 아이디(이메일)")
    @Email
    private String userId;
    @Password
    @Schema(name = "userPw", example = "asdfASDF1!", requiredProperties = "true", description = "비밀번호(영문 대소문자, 숫자 특수문자 최소 한 개씩 포함 9자 이상 500자 이하)")
    private String userPw;
    @Nickname
    @Schema(name = "nickname", example = "ttukttak", requiredProperties = "true", description = "유저 닉네임(4글자 이상 12글자 미만)")
    private String nickname;
    @Schema(name = "adProvision", example = "true", requiredProperties = "true", description = "서비스 홍보 약관")
    private boolean adProvision;

    public Member toMember() {
        return Member.builder()
                .userId(userId)
                .userPw(userPw)
                .nickname(nickname)
                .accountType(AccountType.DEFAULT)
                .adProvision(adProvision)
                .profileImgUrl("https://ttuckttak.s3.ap-northeast-2.amazonaws.com/profile/KakaoTalk_20230905_110330299.png") // Todo: default image url 설정 필요
                .pushApprove(true)
                .refreshToken(null)
                .nightApprove(true)
                .status(true)
                .build();
    }
}
