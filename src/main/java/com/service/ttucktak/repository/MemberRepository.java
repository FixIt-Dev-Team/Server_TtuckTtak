package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByMemberIdx(UUID memberIdx);
    Optional<Member> findByUserId(String userId);

    boolean existsMemberByNickname(String nickname);

    boolean existsMemberByUserId(String userId);

    Member findMembersByUserId(String userId);

}
