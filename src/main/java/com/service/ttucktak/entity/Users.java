package com.service.ttucktak.entity;

import com.service.ttucktak.base.Role;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.UUID;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "users")
@Table(name = "users")
@NoArgsConstructor
public class Users implements UserDetails {


    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID userIdx;

    @Column(name = "userID", length = 45, nullable = false)
    private String userID; // 유저 아이디
    @Column(name = "userPW", length = 500, nullable = false)
    private String userPW; // 유저 비밀번호
    @Column(name = "userName", length = 45, nullable = false)
    private String userName; // 유저 이름
    @Column(name = "email", length = 100, nullable = false)
    private String email; // 유저 이메일

    @Temporal(TemporalType.DATE) // MySQL에 맞게 타입지정
    @Column(name = "birthday", nullable = false)
    private Date birthday; // 유저 생일

    @ColumnDefault("false") // 기본값 지정
    @Column(name = "validation", nullable = false)
    private Boolean validation; // 유저 인증 여부

    @ColumnDefault("0")
    @Column(name = "accountType", nullable = false)
    private Integer accountType; // 계정 타입

    @Column(name = "createdAt")// 기본값 지정
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Row 생성 시점

    @Column(name = "updatedAt")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Row 업데이트 시점

    @Column(name = "status")
    private Boolean status = true; // Row 유효 상태

    public UUID getUserIdx() {
        return userIdx;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Boolean getValidation() {
        return validation;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getStatus() {
        return status;
    }

    @Builder
    public Users(String userID, String userPW, String userName, String email, Date birthday, Boolean validation, Integer accountType, Role role) {
        this.userID = userID;
        this.userPW = userPW;
        this.userName = userName;
        this.email = email;
        this.birthday = birthday;
        this.validation = validation;
        this.accountType = accountType;
    }

    public Users update(String userName, String userEmail, String birthday) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(birthday);
        this.userID = userEmail;
        this.email = userEmail;
        this.userName = userName;
        this.birthday = date;

        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPW;
    }

    @Override
    public String getUsername() {
        return userID;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
