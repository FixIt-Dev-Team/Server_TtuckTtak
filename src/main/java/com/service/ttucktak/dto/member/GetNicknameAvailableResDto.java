package com.service.ttucktak.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetNicknameAvailableResDto {
    @Schema(name = "isAvailable", example = "true", description = "닉네임 사용 가능 여부")
    @JsonProperty("isAvailable")
    boolean isAvailable;
}
