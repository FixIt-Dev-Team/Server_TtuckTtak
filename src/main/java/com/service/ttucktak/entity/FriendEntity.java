package com.service.ttucktak.entity;

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
@Entity(name = "Friend")
@Table(name = "Friend")
@Getter
@NoArgsConstructor
public class FriendEntity {
    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID friendIdx;

    @ManyToOne // Many (친구) One(유저) 한명의 유저는 여러명의 친구를 가질 수 있다.
    @JoinColumn(name = "hostIdx")
    private UserEntity hostIdx; // 친구 소유 인덱슨

    @ManyToOne // Many (친구) One(유저) 한명의 유저는 여러명의 유저에게 친구 대상이 될 수 있다.
    @JoinColumn(name = "targetIdx")
    private UserEntity targetIdx; // 친구 대상 인덱스

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
}
