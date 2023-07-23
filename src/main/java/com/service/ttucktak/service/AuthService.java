package com.service.ttucktak.service;

import com.service.ttucktak.File.FileService;
import com.service.ttucktak.base.AccountType;
import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.*;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
import com.service.ttucktak.utils.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.service.ttucktak.utils.S3Util.Directory.PROFILE;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final FileService fileService;
    private final ProfanityFilterService profanityFilterService;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    private static String defaultUrlImage = "default url";

    /**
     * 회원가입 - 뚝딱 서비스 회원가입
     */
    @Transactional(rollbackFor = BaseException.class)
    public Member signUp(PostSignUpReqDto data) throws BaseException {
        try {
            // 동일한 아이디가 있는지 확인
            // 이미 동일한 아이디가 존재하는 경우 already exist id exception
            if (memberRepository.findByUserId(data.getUserId()).isPresent())
                throw new BaseException(BaseErrorCode.ALREADY_EXIST_ID);

            /// 닉네임 비속어 처리
            // 비속어가 포함되어 있는 경우 invalid nickname exception
            String nickname = data.getNickname();
            if (profanityFilterService.containsProfanity(nickname))
                throw new BaseException(BaseErrorCode.INVALID_NICKNAME);

            // 동일한 닉네임 가지고 있는지 확인
            // 이미 동일한 닉네임을 가지고 있는 경우 already exist nickname exception
            if (memberRepository.existsMemberByNickname(nickname))
                throw new BaseException(BaseErrorCode.ALREADY_EXIST_NICKNAME);

            // 회원 가입 시작
            // 비밀번호 암호화
            String encryptedPw = passwordEncoder.encode(data.getUserPw());
            data.setUserPw(encryptedPw);

            // DB에 저장
            return memberRepository.save(data.toEntity());

        } catch (BaseException e) {
            log.error(e.getErrorCode().getMessage());
            throw e;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 사용 가능한 닉네임인지 확인
     */
    public boolean nicknameAvailable(String nickname) throws BaseException {
        try {
            // 닉네임 형식 validation
            // 닉네임 형식에 맞지 않는 경우 invalid nickname format exception
            if (!RegexUtil.isValidNicknameFormat(nickname))
                throw new BaseException(BaseErrorCode.INVALID_NICKNAME_FORMAT);

            // 닉네임 비속어 처리
            // 비속어가 포함되어 있는 경우 invalid nickname exception
            if (profanityFilterService.containsProfanity(nickname))
                throw new BaseException(BaseErrorCode.INVALID_NICKNAME);

            // 동일한 닉네임 가지고 있는지 확인
            // 이미 동일한 닉네임을 가지고 있는 경우 already exist nickname exception
            if (memberRepository.existsMemberByNickname(nickname))
                throw new BaseException(BaseErrorCode.ALREADY_EXIST_NICKNAME);

            return true;

        } catch (BaseException e) {
            log.warn(e.getErrorCode().getMessage());
            return false;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 로그인 - 사용자 객체를 통한 로그인 (해당 메소드는 회원 가입할 때만 사용해 사용자의 정보를 확인하는 절차가 없음)
     */
    public PostLoginRes login(Member member, String userPW) throws BaseException {
        try {
            // 해당 계정 로그인 처리
            // access token과 refresh token 발급
            TokensDto tokens = generateToken(member.getUserId(), userPW);

            // 사용자의 refresh token 업데이트
            member.updateRefreshToken(tokens.getRefreshToken());

            // 로그인 결과 반환 (userIdx, 토큰 반환)
            String userIdx = member.getMemberIdx().toString();
            return new PostLoginRes(userIdx, tokens);

        } catch (Exception e) {
            e.getCause();
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
            if (!passwordEncoder.matches(userPW, member.getPassword()))
                throw new BaseException(BaseErrorCode.LOGIN_FAILED);

            // 해당 계정 로그인 처리
            // access token과 refresh token 발급
            TokensDto tokens = generateToken(userID, userPW);

            // 사용자의 refresh token 업데이트
            member.updateRefreshToken(tokens.getRefreshToken());

            // 로그인 결과 반환 (userIdx, 토큰 반환)
            String userIdx = member.getMemberIdx().toString();
            return new PostLoginRes(userIdx, tokens);

        } catch (BaseException e) {
            e.getCause();
            log.error(e.getMessage());
            throw new BaseException(e.getErrorCode());
        } catch (Exception e) {
            e.getCause();
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 로그인 - 사용자 id와 pw를 통한 로그인
     */

    public PostLogoutRes logout(UUID userIdx) throws BaseException {

        try {
            Member member = memberRepository.findByMemberIdx(userIdx)
                    .orElseThrow(()->new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

            member.updateRefreshToken(null);

            return new PostLogoutRes(true);

        } catch (BaseException e){

            e.getCause();
            log.error(e.getMessage());
            throw new BaseException(e.getErrorCode());

        } catch (Exception e){

            e.getCause();
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);

        }

    }

    /**
     * 토큰 발행
     */
    public TokensDto generateToken(Object principal, Object credentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtUtil.createTokens(authentication);
    }

    /**
     * 로그인 - 소셜 계정
     */
    public PostLoginRes loginWithSocialAccount(SocialAccountUserInfo data, AccountType type) throws BaseException {
        try {
            // 이메일(user id)을 기반으로 사용자를 조회한다
            // 만약 조회된 결과가 없으면 사용자를 생성하고 DB에 저장한다
            Member member = memberRepository.findByUserId(data.getUserEmail())
                    .orElseGet(() -> {
                        Member newMember = Member.builder()
                                .userId(data.getUserEmail())
                                .userPw(passwordEncoder.encode(data.getUserEmail()))
                                .nickname(data.getUserName()) // Todo: 동명이인 처리???
                                .accountType(type)
                                .adProvision(false)
                                .profileImgUrl(data.getImgURL())
                                .pushApprove(false)
                                .nightApprove(false)
                                .status(true)
                                .build();

                        // image url에 있는 이미지를 다운로드하고 해당 이미지를 S3에 업로드한다.
                        // 업로드 링크를 셋팅한다
                        // 이 과정에서 오류가 발생하면 default image url로 설정한다
                        try {
                            MultipartFile image = fileService.downloadFile(data.getImgURL(), "profile_image", "profile_image.png", "image/png");
                            String uploadedUrl = s3Util.upload(image, PROFILE.getDirectory());
                            newMember.updateProfileImageUrl(uploadedUrl);

                        } catch (Exception e) {
                            log.warn(e.getMessage());
                            log.warn("유저(" + data.getUserName() + ")의 이미지 설정하는 과정에서 이상 발생");
                            newMember.updateProfileImageUrl(defaultUrlImage);
                        }

                        return memberRepository.save(newMember);
                    });

            // --- 로그인 처리 ---
            // 토큰을 발급받고, refresh token을 DB에 저장한다.
            TokensDto token = generateToken(member.getUserId(), member.getUserId());
            member.updateRefreshToken(token.getRefreshToken());

            log.info(member.toString());

            return new PostLoginRes(member.getMemberIdx().toString(), token);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }
}
