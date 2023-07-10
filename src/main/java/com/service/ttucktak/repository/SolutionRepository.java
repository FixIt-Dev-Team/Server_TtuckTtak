package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, UUID> {
}
