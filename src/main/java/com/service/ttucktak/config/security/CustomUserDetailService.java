package com.service.ttucktak.config.security;

import com.service.ttucktak.base.BaseErrorCode;
import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.PostUserDataReqDto;
import com.service.ttucktak.dto.auth.PostUserDataResDto;
import com.service.ttucktak.dto.auth.PutPasswordUpdateDto;
import com.service.ttucktak.dto.member.UserDataDto;
import com.service.ttucktak.entity.Member;
import com.service.ttucktak.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
