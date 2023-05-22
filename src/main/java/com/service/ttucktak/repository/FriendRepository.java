package com.service.ttucktak.repository;

import com.service.ttucktak.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {
}
