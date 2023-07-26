package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostLogoutReq {
    @Schema(name = "userIdx", example = "UUID string", requiredProperties = "true", description = "유저 UUID값으로 변환 가능한 스트링 데이터")
    private String userIdx;
}
