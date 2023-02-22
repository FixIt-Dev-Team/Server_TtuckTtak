package com.service.ttucktak.service;

import com.service.ttucktak.repository.FriendRepository;
import com.service.ttucktak.repository.ProfileRepository;
import com.service.ttucktak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 유저 서비스단 클래스
 * */
@Service
public class UserService {
    /**
     * 접근할 레포지토리
     * */
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FriendRepository friendRepository;

    /**
     * 생성자 의존성 주입 - Constructor Dependency Injection
     * */
    @Autowired
    public UserService(UserRepository userRepository, ProfileRepository profileRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.friendRepository = friendRepository;
    }
}
