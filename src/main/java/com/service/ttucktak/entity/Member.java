package com.service.ttucktak.entity;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.base.BaseEntity;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity(name = "member")
@Table(name = "member")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_idx", nullable = false)
    private UUID memberIdx;

    @Column(name = "user_id", length = 45, nullable = false, unique = true)
    private String userId;

    @Column(name = "user_pw", length = 500, nullable = false)
    private String userPw;

    @Column(name = "nickname", length = 45, nullable = false)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "accountType", nullable = false)
    private AccountType accountType;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    // 서비스 홍보 약관
    @Column(name = "ad_provision", nullable = false)
    private boolean adProvision = false;

    // 이벤트 및 기능추가 Push 알림
    @Column(name = "push_approve", nullable = false)
    private boolean pushApprove = false;

    // 야간시간 Push 알림
    @Column(name = "night_approve", nullable = false)
    private boolean nightApprove = false;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public Member(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, UUID memberIdx, String userId, String userPw, String nickname, AccountType accountType, String profileImgUrl, boolean adProvision, boolean pushApprove, boolean nightApprove, String refreshToken) {
        super(createdAt, updatedAt, status);
        this.memberIdx = memberIdx;
        this.userId = userId;
        this.userPw = userPw;
        this.nickname = nickname;
        this.accountType = accountType;
        this.profileImgUrl = profileImgUrl;
        this.adProvision = adProvision;
        this.pushApprove = pushApprove;
        this.nightApprove = nightApprove;
        this.refreshToken = refreshToken;
    }

    public Member update(String userName, String userEmail) throws ParseException {
        this.userId = userEmail;
        this.nickname = userName;
        return this;
    }

    public void updateUserProfile(PostUserDataReqDto dto) {
        this.nickname = dto.getNickName();
        if (dto.getImgUpdate() != null) {
            this.profileImgUrl = dto.getImgUpdate();
        }
    }

    public void updateProfileImageUrl(String uploadedUrl) {
        this.profileImgUrl = uploadedUrl;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updatePassword(String newPasswordEnc) {
        this.userPw = newPasswordEnc;
    }

    public void updateMemberNickname(String target) {
        this.nickname = target;
    }

    public boolean updatePushApprove(boolean target) {
        if (target != this.pushApprove) {
            this.pushApprove = target;
            return true;
        }
        return false;
    }

    public boolean updateNightApprove(boolean target) {
        if (target != this.nightApprove) {
            this.nightApprove = target;
            return true;
        }
        return false;
    }

    public UUID getMemberIdx() {
        return memberIdx;
    }

    public boolean isPushApprove() {
        return pushApprove;
    }

    public boolean isNightPushApprove() {
        return nightApprove;
    }

    /**
     * UserDetail 구현 메서드
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public String getUsername() {
        return nickname;
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
