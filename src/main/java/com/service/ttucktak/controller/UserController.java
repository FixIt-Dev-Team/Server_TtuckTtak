package com.service.ttucktak.controller;

import com.service.ttucktak.service.UserService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final JwtUtility jwtUtility;

    private final RegexUtility regexUtility;

    private final UserService userService;

    @Autowired
    public UserController(JwtUtility jwtUtility, RegexUtility regexUtility, UserService userService){
        this.jwtUtility = jwtUtility;
        this.regexUtility = regexUtility;
        this.userService = userService;
    }
}
