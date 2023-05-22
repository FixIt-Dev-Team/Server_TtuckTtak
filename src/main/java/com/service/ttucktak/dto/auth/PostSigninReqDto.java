package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.ttucktak.entity.UserEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSigninReqDto {

    private String userID;

    private String userPW;

    private String userName;

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private int accountType;

    public UserEntity toEntity(boolean validation){
        return UserEntity.builder()
                .userID(userID)
                .userPW(userPW)
                .userName(userName)
                .email(email)
                .birthday(birthday)
                .validation(validation)
                .accountType(accountType)
                .build();
    }
}
