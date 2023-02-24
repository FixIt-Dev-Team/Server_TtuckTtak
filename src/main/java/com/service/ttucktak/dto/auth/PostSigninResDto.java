package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
public class PostSigninResDto {
    @JsonProperty("isSuccess")
    boolean isSuccess;
}
