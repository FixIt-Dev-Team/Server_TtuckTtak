package com.service.ttucktak.controller;

import com.service.ttucktak.service.UserService;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final JwtUtil jwtUtil;

    private final RegexUtil regexUtil;

    private final UserService userService;

    @Autowired
    public UserController(JwtUtil jwtUtil, RegexUtil regexUtil, UserService userService){
        this.jwtUtil = jwtUtil;
        this.regexUtil = regexUtil;
        this.userService = userService;
    }
}
