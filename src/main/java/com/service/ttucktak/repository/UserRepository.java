package com.service.ttucktak.repository;

import com.service.ttucktak.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUserIdx(UUID userIdx);

    Optional<Users> findByUserID(String userId);

    @Query("select userIdx from users where userID = :userId")
    Optional<UUID> findUserIdxByUserID(String userId);

    Optional<Users> findByEmail(String email);

    boolean existsUserEntityByUserIdx(UUID userIdx);

    boolean existsUserEntityByUserID(String userID);

//    @Modifying
//    @Transactional
//    @Query(value = "insert into Users (userIdx, userID, userPW, userName, email, birthday, accountType) " +
//            "values(:#{#entity.userIdx}, #{#entity.userID}, :#{#entity.userPW}, :#{#entity.userName}, :#{#entity.email}, :#{#entity.birthday}, :#{#entity.accountType})", nativeQuery = true)
//    void insertNewUser(
//            @Param("entity") UserEntity entity
//    );

}
