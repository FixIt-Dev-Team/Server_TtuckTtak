package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSignUpReqDto {
    @Schema(name = "userId", example = "example@example.com", requiredProperties = "true", description = "유저 아이디(이메일)")
    private String userId;
    @Schema(name = "userPw", example = "12345678", requiredProperties = "true", description = "비밀번호(8자 이상 20자 미만)")
    private String userPw;
    @Schema(name = "nickname", example = "nickname", requiredProperties = "true", description = "유저 닉네임(2자 이상 10자 미만)")
    private String nickname;
    @Schema(name = "accountType", example = "0", requiredProperties = "true", description = "계정 타입(자체:0 카카오: 1, 구글: 2")
    private int accountType;

    public Member toEntity(){
        AccountType type;

        if(accountType == 0) type = AccountType.DEFAULT;
        else if(accountType == 1) type = AccountType.KAKAO;
        else type = AccountType.GOOGLE;

        return Member.builder()
                .userId(userId)
                .userPw(userPw)
                .nickname(nickname)
                .accountType(type)
                .build();
    }
}
