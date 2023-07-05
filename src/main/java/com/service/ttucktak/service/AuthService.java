package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.*;
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
import org.springframework.security.core.parameters.P;
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

    @Transactional(rollbackFor = BaseException.class)
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

    public TokensDto loginToken(String userID, String userPW) throws BaseException {

        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userID, userPW);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            return jwtUtil.createTokens(authentication);
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.LOGIN_FAILED);
        }


    }

    public UUID loginUserIdx(String userID) throws BaseException {
        Optional<UUID> userIdx = userRepository.findUserIdxByUserID(userID);
        userIdx.orElseThrow(() -> new BaseException(BaseErrorCode.USER_NOT_FOUND));

        return userIdx.get();
    }

    public PostLoginRes kakaoOauth2(KakaoUserDto kakaoUserDto) throws BaseException {
        boolean userExist = false;

        try{
            userExist = userRepository.existsUsersByUserID(kakaoUserDto.getUserEmail());
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        //유저 미 존재시 회원가입 후 로그인 처리
        if(!userExist) {
            try{
                PostSignUpReqDto postSignUpReqDto =
                        new PostSignUpReqDto(kakaoUserDto.getUserEmail(), passwordEncoder.encode(kakaoUserDto.getUserEmail()),
                                kakaoUserDto.getUserName(), kakaoUserDto.getUserEmail(),
                                kakaoUserDto.getBirthday(), 1);

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
        }

        //로그인 처리
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoUserDto.getUserEmail(), kakaoUserDto.getUserEmail());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokensDto token = jwtUtil.createTokens(authentication);

            UUID userIdx = loginUserIdx(kakaoUserDto.getUserEmail());

            return new PostLoginRes(userIdx.toString(), token);
        }
        catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.LOGIN_FAILED);
        }
    }

    public PostLoginRes googleOauth2(GoogleUserDto googleUserDto) throws BaseException {
        boolean userExist = false;

        try{
            userExist = userRepository.existsUsersByUserID(googleUserDto.getUserEmail());
        }catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        //유저 미 존재시 회원가입 후 로그인 처리
        if(!userExist) {
            try{
                PostSignUpReqDto postSignUpReqDto =
                        new PostSignUpReqDto(googleUserDto.getUserEmail(), passwordEncoder.encode(googleUserDto.getUserEmail()),
                                googleUserDto.getUserName(), googleUserDto.getUserEmail(),
                                googleUserDto.getBirthday(), 1);

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
        }

        //로그인 처리
        try{
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(googleUserDto.getUserEmail(), googleUserDto.getUserEmail());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokensDto token = jwtUtil.createTokens(authentication);

            UUID userIdx = loginUserIdx(googleUserDto.getUserEmail());

            return new PostLoginRes(userIdx.toString(), token);
        }
        catch (Exception exception){
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.LOGIN_FAILED);
        }
    }
}
