package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginReq {
    @Schema(name = "userId", example = "ttukttak@ttukttak.com", requiredProperties = "true", description = "유저 아이디(이메일)")
    private String userId;

    @Schema(name = "userPw", example = "asdfASDF1!", requiredProperties = "true", description = "비밀번호(8자 이상 20자 미만)")
    private String userPw;
}
