package com.service.ttucktak.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatchUserDataReqDto {

    @Schema(name = "memberIdx", example = "UUID to string", requiredProperties = "true", description = "변경할 유저 UUID")
    private String memberIdx;
    @Schema(name = "nickName", example = "WaGaNaWaMGM", requiredProperties = "true", description = "변경할 유저 닉네임")
    private String nickName;

}
