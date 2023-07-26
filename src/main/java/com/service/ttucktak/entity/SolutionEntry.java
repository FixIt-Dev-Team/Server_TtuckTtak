package com.service.ttucktak.entity;

import com.service.ttucktak.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
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

    @Column(name = "survey_idx", nullable = false)
    private Long surveyIdx;

    @Column(name = "res_pattern", nullable = false)
    private Long resPattern;

    @Column(name = "issue_type", nullable = false)
    private Long issueType;

    @Column(name = "problem_name", nullable = false)
    private String problemName;

    @Builder
    public SolutionEntry(LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, Long entryIdx, Long surveyIdx, Long resPattern, Long issueType, String problemName) {
        super(createdAt, updatedAt, status);
        this.entryIdx = entryIdx;
        this.surveyIdx = surveyIdx;
        this.resPattern = resPattern;
        this.issueType = issueType;
        this.problemName = problemName;
    }
}
