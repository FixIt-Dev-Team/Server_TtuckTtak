package com.service.ttucktak.repository;

import com.service.ttucktak.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@ApiIgnore
@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
}
