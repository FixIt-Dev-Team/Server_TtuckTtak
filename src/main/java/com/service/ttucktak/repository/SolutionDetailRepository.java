package com.service.ttucktak.repository;

import com.service.ttucktak.entity.SolutionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolutionDetailRepository extends JpaRepository<SolutionDetail, UUID> {

    public Optional<SolutionDetail> findBySolutionIdx_SolutionIdx(UUID solutionIdx);

}
