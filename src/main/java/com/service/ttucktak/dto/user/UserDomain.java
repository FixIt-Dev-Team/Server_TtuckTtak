package com.service.ttucktak.dto.user;

import com.service.ttucktak.base.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDomain {
    private String userId;
    private String userPw;
    private String userName;
    private String email;
    private String birthday;
    private int accountType;
    private Role role;

    @Builder
    public UserDomain(String userId, String userPw, String userName, String email, String birthday, int accountType, Role role) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.email = email;
        this.birthday = birthday;
        this.accountType = accountType;
        this.role = role;
    }
}
