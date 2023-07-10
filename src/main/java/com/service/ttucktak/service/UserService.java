package com.service.ttucktak.service;

import com.service.ttucktak.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 유저 서비스단 클래스
 * */
@Service
public class UserService {
    private final MemberRepository memberRepository;

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     * */
    @Autowired
    public UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
