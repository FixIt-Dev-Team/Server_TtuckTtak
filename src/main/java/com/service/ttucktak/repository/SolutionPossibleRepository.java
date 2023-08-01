package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Solution;
import com.service.ttucktak.entity.SolutionEntry;
import com.service.ttucktak.entity.SolutionPossible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolutionPossibleRepository extends JpaRepository<SolutionPossible, UUID> {

    List<SolutionPossible> findByEntryIdx_EntryIdx(Long entryIdx);
}
