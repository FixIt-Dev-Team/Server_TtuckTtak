package com.service.ttucktak.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutPasswordUpdateDto {

    @Schema(name = "userIdx", example = "UUID to string", requiredProperties = "true", description = "변경할 유저 UUID")
    private String userIdx;
    @Schema(name = "new PassWord", example = "12345678!A", requiredProperties = "true", description = "요청된 패스워드 업데이트")
    private String newPw;

}
