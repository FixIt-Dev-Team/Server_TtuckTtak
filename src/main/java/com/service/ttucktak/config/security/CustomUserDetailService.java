package com.service.ttucktak.config.security;

import com.service.ttucktak.entity.Users;
import com.service.ttucktak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        return userRepository.findByUserID(userId)
                .map(this::getUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
    }

    private UserDetails getUserDetails(Users user){
        return User.builder()
                .username(user.getUserID())
                .password(user.getUserPW())
                .roles(user.getRole().toString())
                .build();
    }
}
