package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.PostSigninReqDto;
import com.service.ttucktak.dto.auth.PostSigninResDto;
import com.service.ttucktak.entity.ProfileEntity;
import com.service.ttucktak.entity.UserEntity;
import com.service.ttucktak.repository.ProfileRepository;
import com.service.ttucktak.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class AuthService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    @Autowired
    public AuthService(UserRepository userRepository, ProfileRepository profileRepository){
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public PostSigninResDto createUsers(PostSigninReqDto postSigninReqDto) throws BaseException {

        try{
            UserEntity entity = postSigninReqDto.toEntity(false);

            UUID userIdx = userRepository.save(entity).getUserIdx();
            UserEntity insertedEntity = userRepository.findUserEntityByUserIdx(userIdx);

            ProfileEntity profileEntity =
                    ProfileEntity.builder()
                            .userIdx(insertedEntity)
                            .iconType(0)
                            .nickName(insertedEntity.getUserName())
                            .build();

            profileRepository.save(profileEntity);

        }catch (Exception exception){
            logger.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostSigninResDto(true);
    }
}
