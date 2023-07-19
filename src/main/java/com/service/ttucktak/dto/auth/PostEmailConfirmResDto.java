package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostEmailConfirmResDto {
    @Schema(name = "code", example = "12345678", description = "이메일 인증 코드")
    String code;
}