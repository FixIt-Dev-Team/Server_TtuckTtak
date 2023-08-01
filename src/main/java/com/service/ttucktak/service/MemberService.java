package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.member.GetNicknameAvailableResDto;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.auth.PutPasswordUpdateDto;
import com.service.ttucktak.dto.member.*;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 유저 서비스단 클래스
 */
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     */
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용 가능한 닉네임인지 확인
     */
    @Transactional(readOnly = true)
    public GetNicknameAvailableResDto nicknameAvailable(String nickname) throws BaseException {
        try {
            // 동일한 닉네임 가지고 있는지 확인
            // 이미 동일한 닉네임을 가지고 있는 경우 사용 불가, 없는 경우는 사용 가능
            return new GetNicknameAvailableResDto(!memberRepository.existsMemberByNickname(nickname));

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 닉네임 변경 API - Service
     */
    public PatchNicknameResDto patchNickname(PatchNicknameReqDto req) throws BaseException {
        //변경 대상 멤버 가져오기
        Member member = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()))
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 닉네임 변경
        member.updateMemberNickname(req.getNickname());

        //리턴
        return new PatchNicknameResDto(member.getNickname());
    }

    /**
     * 사용자 정보 로드 API
     */
    public UserDataDto loadUserByUUID(UUID userIdx) throws BaseException {
        Member currentUser = memberRepository.findByMemberIdx(userIdx)
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        return new UserDataDto(currentUser);
    }

    /**
     * Push 알람 설정 API - Service
     */
    public PatchNoticeResDto patchPushNotice(PatchNoticeReqDto req) throws BaseException {
        //변경 대상 멤버 가져오기
        Member member = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()))
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 알림 설정 허용여부 변경 (같을 경우 바꾸지 않고 바꿀 필요 없다는 플레그 세우기)
        boolean needToChange = member.updatePushApprove(req.isTargetStatus());

        //바꿀필요 없다면 트랜잭션 수행하지 않고 에러 던지기
        if (!needToChange) {
            throw new BaseException(BaseErrorCode.NOTICE_REQ_ERROR);
        }

        //리턴
        return new PatchNoticeResDto(member.isPushApprove());
    }

    /**
     * 야간 알람 설정 API - Service
     */
    public PatchNoticeResDto patchNightNotice(PatchNoticeReqDto req) throws BaseException {
        //변경 대상 멤버 가져오기
        Member member = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()))
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 야간 알림 설정 허용여부 변경 (같을 경우 바꾸지 않고 바꿀 필요 없다는 플레그 세우기)
        boolean needToChange = member.updateNightApprove(req.isTargetStatus());

        //바꿀필요 없다면 트랜잭션 수행하지 않고 에러 던지기
        if (!needToChange) {
            throw new BaseException(BaseErrorCode.NOTICE_REQ_ERROR);
        }

        //리턴
        return new PatchNoticeResDto(member.isPushApprove());
    }

    public PostUserDataResDto updateUserByUUID(UUID userIdx, PostUserDataReqDto dto) throws BaseException {
        Member currentUser = memberRepository.findByMemberIdx(userIdx)
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        try {
            currentUser.updateUserProfile(dto);
        } catch (Exception exception) {
            log.error("Member update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.MEMBER_ERROR);
        }

        try {
            memberRepository.save(currentUser);
        } catch (Exception exception) {
            log.error("Member database update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostUserDataResDto(true);

    }

    public PostUserDataResDto updateUserPasswordByUUID(UUID userIdx, PutPasswordUpdateDto dto) throws BaseException {
        Member currentUser = memberRepository.findByMemberIdx(userIdx)
                .orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        try {
            currentUser.updatePassword(passwordEncoder.encode(dto.getNewPw()));
        } catch (Exception exception) {
            log.error("Member PW update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.PWUPDATE_ERROR);
        }

        try {
            memberRepository.save(currentUser);
        } catch (Exception exception) {
            log.error("Member database update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostUserDataResDto(true);
    }
}
