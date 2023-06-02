package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.PostSignUpReqDto;
import com.service.ttucktak.dto.auth.PostSignUpResDto;
import com.service.ttucktak.dto.auth.TokensDto;
import com.service.ttucktak.entity.Profile;
import com.service.ttucktak.entity.Users;
import com.service.ttucktak.repository.ProfileRepository;
import com.service.ttucktak.repository.UserRepository;
import com.service.ttucktak.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, ProfileRepository profileRepository, JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PostSignUpResDto createUsers(PostSignUpReqDto postSignUpReqDto) throws BaseException {

        try{
            postSignUpReqDto.setUserPW(passwordEncoder.encode(postSignUpReqDto.getUserPW()));
            Users entity = postSignUpReqDto.toEntity(false);

            UUID userIdx = userRepository.save(entity).getUserIdx();
            Optional<Users> insertedEntity = userRepository.findByUserIdx(userIdx);
            insertedEntity.orElseThrow(() -> new BaseException(BaseErrorCode.USER_NOT_FOUND));
            Profile profile =
                    Profile.builder()
                            .usersIdx(insertedEntity.get())
                            .iconType(0)
                            .nickName(insertedEntity.get().getUserName())
                            .build();

            profileRepository.save(profile);

        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostSignUpResDto(true);
    }

    @Transactional
    public TokensDto loginToken(String userID, String userPW){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userID, userPW);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtUtil.createTokens(authentication);

    }

    @Transactional
    public UUID loginUserIdx(String userID) throws BaseException {
        Optional<UUID> userIdx = userRepository.findUserIdxByUserID(userID);
        userIdx.orElseThrow(() -> new BaseException(BaseErrorCode.USER_NOT_FOUND));

        return userIdx.get();
    }
}
