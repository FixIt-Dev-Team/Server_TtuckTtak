package com.service.ttucktak.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchNicknameResDto {
    @Schema(name = "nickname", example = "nickname", description = "변경한 닉네임")
    String nickname;
}
