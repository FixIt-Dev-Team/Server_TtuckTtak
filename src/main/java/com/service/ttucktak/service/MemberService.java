package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
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

import java.util.Optional;
import java.util.UUID;

/**
 * 유저 서비스단 클래스
 * */
@Service
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     * */
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 닉네임 변경 API - Service
     * */
    public PatchNicknameResDto patchNickname(PatchNicknameReqDto req) throws BaseException{
        //변경 대상 멤버 가져오기
        Optional<Member> target = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()));
        target.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 닉네임 변경
        target.get().updateMemberNickname(req.getNickname());

        //리턴
        return new PatchNicknameResDto(target.get().getNickname());
    }

    /**
     * 사용자 정보 로드 API
     * */
    public UserDataDto loadUserByUUID(UUID userIdx) throws BaseException {
        Optional<Member> res = memberRepository.findByMemberIdx(userIdx);

        Member currentUser = res.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        UserDataDto user = UserDataDto.builder()
                .userName(currentUser.getUsername())
                .email(currentUser.getUserId())
                .profileImgUrl(currentUser.getProfileImgUrl())
                .accountType(currentUser.getAccountType())
                .build();

        return user;
    }

    /**
     * Push 알람 설정 API - Service
     * */
    public PatchNoticeResDto patchPushNotice(PatchNoticeReqDto req) throws BaseException {
        //변경 대상 멤버 가져오기
        Optional<Member> target = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()));
        target.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 알림 설정 허용여부 변경 (같을 경우 바꾸지 않고 바꿀 필요 없다는 플레그 세우기)
        boolean needToChange = target.get().updatePushApprove(req.isTargetStatus());

        //바꿀필요 없다면 트랜잭션 수행하지 않고 에러 던지기
        if(!needToChange){
            throw new BaseException(BaseErrorCode.NOTICE_REQ_ERROR);
        }

        //리턴
        return new PatchNoticeResDto(target.get().isPushApprove());
    }

    /**
     * 야간 알람 설정 API - Service
     * */
    public PatchNoticeResDto patchNightNotice(PatchNoticeReqDto req) throws BaseException {
        //변경 대상 멤버 가져오기
        Optional<Member> target = memberRepository.findByMemberIdx(UUID.fromString(req.getMemberIdx()));
        target.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_ERROR));

        //멤버 야간 알림 설정 허용여부 변경 (같을 경우 바꾸지 않고 바꿀 필요 없다는 플레그 세우기)
        boolean needToChange = target.get().updateNightApprove(req.isTargetStatus());

        //바꿀필요 없다면 트랜잭션 수행하지 않고 에러 던지기
        if(!needToChange){
            throw new BaseException(BaseErrorCode.NOTICE_REQ_ERROR);
        }

        //리턴
        return new PatchNoticeResDto(target.get().isPushApprove());
    }

    public PostUserDataResDto updateUserByUUID(UUID userIdx, PostUserDataReqDto dto) throws BaseException {

        Optional<Member> res = memberRepository.findByMemberIdx(userIdx);

        Member currentUser = res.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        try{
            currentUser.update(dto);
        }catch (Exception exception){
            log.error("Member update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.MEMBER_ERROR);
        }


        try{
            memberRepository.save(currentUser);
        }catch (Exception exception){
            log.error("Member database update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostUserDataResDto(true);

    }

    public PostUserDataResDto updateUserPasswordByUUID(UUID userIdx, PutPasswordUpdateDto dto) throws BaseException {

        Optional<Member> res = memberRepository.findByMemberIdx(userIdx);

        Member currentUser = res.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        try{
            currentUser.updatePassword(passwordEncoder.encode(dto.getNewPw()));
        }catch (Exception exception){
            log.error("Member PW update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.PWUPDATE_ERROR);
        }
        try{
            memberRepository.save(currentUser);
        }catch (Exception exception){
            log.error("Member database update중 문제 발생 : " + exception.getMessage());
            throw new BaseException(BaseErrorCode.DATABASE_ERROR);
        }

        return new PostUserDataResDto(true);

    }
}
