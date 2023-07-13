package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

// Todo: 회원 가입 과정에서 소셜 로그인은 현재 서비스 홍보 약관에 대한 항목을 받지 않아 충돌 발생. 임시로 social sign up으로 변경. 추후 코드 확인하고 수정

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSocialSignUpReqDto {
    @Schema(name = "userId", example = "example@example.com", requiredProperties = "true", description = "유저 아이디(이메일)")
    private String userId;
    @Schema(name = "userPw", example = "12345678", requiredProperties = "true", description = "비밀번호(영문 대소문자, 숫자 특수문자 최소 한 개씩 포함 9자 이상 500자 이하)")
    private String userPw;
    @Schema(name = "nickname", example = "nickname", requiredProperties = "true", description = "유저 닉네임(4글자 이상 12글자 미만)")
    private String nickname;
    @Schema(name = "accountType", example = "1", requiredProperties = "true", description = "계정 타입(자체: 0 카카오: 1, 구글: 2")
    private int accountType;

    public Member toEntity() {
        AccountType type;

        if (accountType == 1) type = AccountType.KAKAO;
        else if (accountType == 2) type = AccountType.GOOGLE;
        else type = AccountType.DEFAULT;

        return Member.builder()
                .userId(userId)
                .userPw(userPw)
                .nickname(nickname)
                .accountType(type)
                .build();
    }
}
