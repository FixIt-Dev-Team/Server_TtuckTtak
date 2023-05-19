package com.service.ttucktak.dto.auth;

import com.service.ttucktak.base.Role;
import com.service.ttucktak.dto.user.UserDomain;
import com.service.ttucktak.entity.UserEntity;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class SessionUser implements Serializable {
    private final String userName;
    private final String userEmail;
    private final Date birthday;

    public SessionUser(UserEntity user) {
        this.userName = user.getUserName();
        this.userEmail = user.getEmail();
        this.birthday = user.getBirthday();
    }
}
