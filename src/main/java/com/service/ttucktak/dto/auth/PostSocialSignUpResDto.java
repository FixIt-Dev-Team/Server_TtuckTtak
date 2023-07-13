package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

// Todo: 회원 가입 과정에서 소셜 로그인은 현재 서비스 홍보 약관에 대한 항목을 받지 않아 충돌 발생. 임시로 social sign up으로 변경. 추후 코드 확인하고 수정

@Data
@AllArgsConstructor
public class PostSocialSignUpResDto {
    @Schema(name = "isSuccess", example = "true", description = "회원가입 성공 여부")
    @JsonProperty("isSuccess")
    boolean isSuccess;
}
