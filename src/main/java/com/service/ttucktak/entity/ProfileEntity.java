package com.service.ttucktak.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@ApiIgnore
@DynamicInsert
@DynamicUpdate
@Getter
@Entity(name = "profile")
@Table(name = "profile")
@NoArgsConstructor
public class ProfileEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID profileIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private UserEntity userIdx;

    @Column(name = "iconType", nullable = false)
    private Integer iconType;

    @ColumnDefault("'프로필 설명을 입력해 주세요'")
    @Column(name = "profileDesc", length = 500)
    private String profileDesc = "'프로필 설명을 입력해 주세요'";

    @Column(name = "nickName", length = 45, nullable = false)
    private String nickName;

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

    @Builder
    public ProfileEntity(UserEntity userIdx, Integer iconType, String nickName) {
        this.userIdx = userIdx;
        this.iconType = iconType;
        this.nickName = nickName;
    }
}
