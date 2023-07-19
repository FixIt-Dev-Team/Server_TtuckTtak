package com.service.ttucktak.entity;

import com.service.ttucktak.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "solution_entry")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionEntry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_idx")
    private Long entryIdx;

    @Column(name = "res_pattern", nullable = false)
    private Long resPattern;

    @Column(name = "issue_type", nullable = false)
    private Long issueType;

    @Builder
    public SolutionEntry(Date createdAt, Date updatedAt, Boolean status, Long entryIdx, Long resPattern, Long issueType) {
        super(createdAt, updatedAt, status);
        this.entryIdx = entryIdx;
        this.resPattern = resPattern;
        this.issueType = issueType;
    }
}
