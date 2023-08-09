package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRefreshTokenDto {
    @Schema(name = "refreshToken", example = "refreshToken String", description = "리프레시 토큰")
    private String refreshToken;
    @Schema(name = "userIdx", example = "userIdx UUID String", description = "갱신 요청하는 유저 인덱스")
    private String userIdx;
}
