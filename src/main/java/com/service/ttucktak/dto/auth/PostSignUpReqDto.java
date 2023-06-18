package com.service.ttucktak.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.service.ttucktak.base.Role;
import com.service.ttucktak.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostSignUpReqDto {
    @Schema(name = "userID", example = "example@example.com", requiredProperties = "true", description = "유저 아이디(이메일)")
    private String userID;
    @Schema(name = "userPW", example = "12345678", requiredProperties = "true", description = "비밀번호(8자 이상 20자 미만)")
    private String userPW;
    @Schema(name = "userName", example = "userName", requiredProperties = "true", description = "유저 닉네임(2자 이상 10자 미만)")
    private String userName;
    @Schema(name = "email", example = "example@example.com", requiredProperties = "true", description = "유저 이메일")
    private String email;

    @Schema(name = "birthday", example = "1998-11-14", requiredProperties = "true", description = "유저 생년월일")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @Schema(name = "accountType", example = "0", requiredProperties = "true", description = "계정 타입(자체:0 카카오: 1, 구글: 2")
    private int accountType;

    public Users toEntity(boolean validation){
        return Users.builder()
                .userID(userID)
                .userPW(userPW)
                .userName(userName)
                .email(email)
                .birthday(birthday)
                .validation(validation)
                .accountType(accountType)
                .role(Role.ROLE_USER)
                .build();
    }
}
