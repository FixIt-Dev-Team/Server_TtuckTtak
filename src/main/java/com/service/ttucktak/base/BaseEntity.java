package com.service.ttucktak.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * 모든 엔티티에 공통으로 들어가는 요소
 * @author LEE JIHO
 * @usage 각 엔티티데 BaseEntity를 extends 받아 사용
 * */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity {

    @Column(name = "createdAt", nullable = false, updatable = false)// 기본값 지정
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Row 생성 시점

    @Column(name = "updatedAt", nullable = false)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt; // Row 업데이트 시점

    @Column(name = "status", nullable = false)
    private Boolean status = true; // Row 유효 상태


    public BaseEntity(Date createdAt, Date updatedAt, Boolean status) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
