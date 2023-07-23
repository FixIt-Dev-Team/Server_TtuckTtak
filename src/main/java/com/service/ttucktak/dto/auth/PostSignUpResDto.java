package com.service.ttucktak.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSignUpResDto {
    private String userIdx;
    private TokensDto tokenInfo;
}