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
@Table(name = "solution_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolutionDetail extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "detail_idx", nullable = false)
    private UUID detailIdx;

    @JoinColumn(name = "sol_idx", referencedColumnName = "sol_idx", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Solution solutionIdx;

    @Column(name = "detail_header", length = 500, nullable = false)
    private String detailHeader;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "sol_order", nullable = false)
    private Integer SolutionOrder;

    @Builder
    public SolutionDetail(Date createdAt, Date updatedAt, Boolean status, UUID detailIdx, Solution solutionIdx, String detailHeader, String content, Integer solutionOrder) {
        super(createdAt, updatedAt, status);
        this.detailIdx = detailIdx;
        this.solutionIdx = solutionIdx;
        this.detailHeader = detailHeader;
        this.content = content;
        SolutionOrder = solutionOrder;
    }
}
