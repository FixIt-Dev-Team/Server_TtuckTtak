package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Solution;
import com.service.ttucktak.entity.SolutionBypass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolutionBypassRepository extends JpaRepository<SolutionBypass, UUID> {

    public List<SolutionBypass> findByStartEntryIdx(Long startEntryIdx);

}
