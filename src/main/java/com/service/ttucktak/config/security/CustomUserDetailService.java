package com.service.ttucktak.config.security;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.auth.PutPasswordUpdateDto;
import com.service.ttucktak.dto.user.UserDataDto;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

/**
 * 유저 인증 구현체
 * @author LEE JIHO
 * */
@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailService(MemberRepository memberRepository,PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        return memberRepository.findMembersByUserId(userId);
    }

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

    public PostUserDataResDto updateUserByUUID(UUID userIdx, PostUserDataReqDto dto) throws BaseException {

        Optional<Member> res = memberRepository.findByMemberIdx(userIdx);

        Member currentUser = res.orElseThrow(() -> new BaseException(BaseErrorCode.DATABASE_NOTFOUND));

        try{
            currentUser.update(dto.getNickName(),currentUser.getUserId());
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
            currentUser.updateCriticalSection(passwordEncoder.encode(dto.getNewPw()));
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
