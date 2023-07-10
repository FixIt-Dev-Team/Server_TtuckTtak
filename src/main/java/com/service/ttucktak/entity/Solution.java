package com.service.ttucktak.entity;

import com.service.ttucktak.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "solution")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Solution extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "sol_idx", nullable = false)
    private UUID solutionIdx;

    @Column(name = "issue_type", nullable = false)
    private Long issueType;

    @Column(name = "desc_header", length = 500, nullable = false)
    private String descHeader;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Builder
    public Solution(Date createdAt, Date updatedAt, Boolean status, UUID solutionIdx, Long issueType, String descHeader, Integer level) {
        super(createdAt, updatedAt, status);
        this.solutionIdx = solutionIdx;
        this.issueType = issueType;
        this.descHeader = descHeader;
        this.level = level;
    }
}
