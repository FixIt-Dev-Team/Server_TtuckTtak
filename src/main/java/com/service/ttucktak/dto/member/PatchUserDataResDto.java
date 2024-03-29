package com.service.ttucktak.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchUserDataResDto {

    @Schema(name = "isSuccess", example = "true", description = "")
    @JsonProperty("isSuccess")
    boolean isSuccess;

    @Schema(name = "userData",description = "updated user data (nullable)")
    UserDataDto userData;

}
