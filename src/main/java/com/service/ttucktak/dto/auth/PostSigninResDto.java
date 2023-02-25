package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema
@AllArgsConstructor
public class PostSigninResDto {
    @JsonProperty("isSuccess")
    boolean isSuccess;
}
