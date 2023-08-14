package com.service.ttucktak.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchUserDataResDto {

    @Schema(name = "isSuccess", example = "true", description = "회원가입 성공 여부")
    @JsonProperty("isSuccess")
    boolean isSuccess;

}
