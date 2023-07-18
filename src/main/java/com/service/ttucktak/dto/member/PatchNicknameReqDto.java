package com.service.ttucktak.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchNicknameReqDto {
    @Schema(name = "memberIdx", example = "Member UUID", requiredProperties = "true", description = "유저 인덱스(UUID)")
    private String memberIdx;
    @Schema(name = "nickname", example = "nickname", requiredProperties = "true", description = "변경 할 닉네임")
    private String nickname;
}
