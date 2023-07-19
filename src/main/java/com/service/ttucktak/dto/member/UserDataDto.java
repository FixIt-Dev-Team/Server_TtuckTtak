package com.service.ttucktak.dto.member;

import com.service.ttucktak.base.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDataDto {
    private String userName;
    private String email;
    private String profileImgUrl;
    private AccountType accountType;

    @Builder
    public UserDataDto(String userName, String email, String birthday, String profileImgUrl, AccountType accountType){
        this.userName = userName;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
        this.accountType = accountType;
    }
}
