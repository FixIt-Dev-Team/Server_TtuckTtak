package com.service.ttucktak.service;

import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder, PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입 - 뚝딱 서비스 회원가입
     */
    @Transactional(rollbackFor = BaseException.class)
    public PostSignUpResDto signUp(PostSignUpReqDto data) throws BaseException {
        try {
            // 동일한 닉네임 가지고 있는지 확인
            // 이미 동일한 닉네임을 가지고 있는 경우 already exist nickname exception
            checkNicknameExists(data.getNickname());

            // 회원 가입 시작
            // 비밀번호 암호화
            String encryptedPw = passwordEncoder.encode(data.getUserPw());
            data.setUserPw(encryptedPw);

            // DB에 저장
            Member member = data.toEntity();
            memberRepository.save(member);

            return new PostSignUpResDto(true);

        } catch (BaseException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 이미 존재하는 닉네임인지 확인
     */
    public void checkNicknameExists(String nickname) throws BaseException {
        try {
            if (memberRepository.findByNickname(nickname).isPresent())
                throw new BaseException(BaseErrorCode.ALREADY_EXIST_NICKNAME);

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 로그인 - 사용자 id와 pw를 통한 로그인
     */
    public PostLoginRes login(String userID, String userPW) throws BaseException {
        try {
            // user Id를 기반으로 회원이 있는지 조회
            // 조회되는 유저가 없다면 login failed exception
            Member member = memberRepository.findByUserId(userID)
                    .orElseThrow(() -> new BaseException(BaseErrorCode.LOGIN_FAILED));

            // 조회된 유저와 비밀번호가 일치하는지 확인
            // 비밀번호가 일치하지 않는 경우 login failed exception
            if (passwordEncoder.matches(userPW, member.getPassword()))
                throw new BaseException(BaseErrorCode.LOGIN_FAILED);

            // 해당 계정 로그인 처리
            // access token과 refresh token 발급
            // Todo: principal과 credentials에 어떤 값을 넣어줄지 고민해볼 것
            TokensDto tokens = generateToken(member.getUserId(), member.getUserId());

            // 사용자의 refresh token 업데이트
            member.updateRefreshToken(tokens.getRefreshToken());

            // 로그인 결과 반환 (userIdx, 토큰 반환)
            String userIdx = member.getMemberIdx().toString();
            return new PostLoginRes(userIdx, tokens);

        } catch (BaseException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 로그인한 멤버 인덱스 가져오기
     */
    public UUID loginUserIdx(String userID) throws BaseException {
        Optional<UUID> memberIdx = memberRepository.findMemberIdxById(userID);
        memberIdx.orElseThrow(() -> new BaseException(BaseErrorCode.USER_NOT_FOUND));

        return memberIdx.get();
    }

    /**
     * 토큰 발행
     */
    public TokensDto generateToken(Object principal, Object credentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtUtil.createTokens(authentication);
    }

    public PostLoginRes kakaoOauth2(KakaoUserDto kakaoUserDto) throws BaseException {
        boolean userExist;

        try {
            userExist = memberRepository.existsMemberByUserId(kakaoUserDto.getUserEmail());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        // 유저 미 존재시 회원가입 후 로그인 처리
        if (!userExist) {
            try {
                Member member = Member.builder()
                        .userId(kakaoUserDto.getUserEmail())
                        .userPw(passwordEncoder.encode(kakaoUserDto.getUserEmail()))
                        .nickname(kakaoUserDto.getUserName())
                        .accountType(AccountType.KAKAO)
                        .build();

                memberRepository.save(member);

            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new BaseException(BaseErrorCode.DATABASE_ERROR);
            }
        }

        //로그인 처리
        try {
            TokensDto token = generateToken(kakaoUserDto.getUserEmail(), kakaoUserDto.getUserEmail());
            UUID userIdx = loginUserIdx(kakaoUserDto.getUserEmail());

            // Todo: refresh token 설정

            return new PostLoginRes(userIdx.toString(), token);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.LOGIN_FAILED);
        }
    }

    public PostLoginRes googleOauth2(GoogleUserDto googleUserDto) throws BaseException {
        boolean userExist;

        try {
            userExist = memberRepository.existsMemberByUserId(googleUserDto.getUserEmail());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        // 유저 미 존재시 회원가입 후 로그인 처리
        if (!userExist) {
            try {
                Member member = Member.builder()
                        .userId(googleUserDto.getUserEmail())
                        .userPw(passwordEncoder.encode(googleUserDto.getUserEmail()))
                        .nickname(googleUserDto.getUserName())
                        .accountType(AccountType.GOOGLE)
                        .build();

                memberRepository.save(member);

            } catch (Exception exception) {
                log.error(exception.getMessage());
                throw new BaseException(BaseErrorCode.DATABASE_ERROR);
            }
        }

        //로그인 처리
        try {
            TokensDto token = generateToken(googleUserDto.getUserEmail(), googleUserDto.getUserEmail());
            UUID userIdx = loginUserIdx(googleUserDto.getUserEmail());

            // Todo: refresh token 설정

            return new PostLoginRes(userIdx.toString(), token);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseErrorCode.LOGIN_FAILED);
        }
    }
}
