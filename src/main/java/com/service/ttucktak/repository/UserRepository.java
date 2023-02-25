package com.service.ttucktak.repository;

import com.service.ttucktak.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@ApiIgnore
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findUserEntityByUserIdx(UUID userIdx);

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
