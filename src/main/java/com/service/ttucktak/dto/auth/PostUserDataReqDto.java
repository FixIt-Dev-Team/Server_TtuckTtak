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
public class PostUserDataReqDto {

    @Schema(name = "userIdx", example = "UUID to string", requiredProperties = "true", description = "변경할 유저 UUID")
    private String userIdx;
    @Schema(name = "nickName", example = "WaGaNaWaMGM", requiredProperties = "true", description = "변경할 유저 닉네임")
    private String nickName;
    @Schema(name = "imgUrl", example = "12345678", requiredProperties = "true", description = "업로드된 ")
    private boolean imgUpdate;

}
