package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.member.PatchNicknameReqDto;
import com.service.ttucktak.dto.member.PatchNicknameResDto;
import com.service.ttucktak.dto.member.PatchNoticeReqDto;
import com.service.ttucktak.dto.member.PatchNoticeResDto;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     * */
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}
