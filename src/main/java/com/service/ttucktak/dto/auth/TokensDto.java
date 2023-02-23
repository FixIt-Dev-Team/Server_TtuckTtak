package com.service.ttucktak.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}
