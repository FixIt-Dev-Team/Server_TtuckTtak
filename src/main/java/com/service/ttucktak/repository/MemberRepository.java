package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByMemberIdx(UUID memberIdx);
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByNickname(String nickname);

    @Query("select memberIdx from member where userId = :userId")
    Optional<UUID> findMemberIdxById(String userId);
    boolean existsMemberByUserId(String userId);

    Member findMembersByUserId(String userId);


}
