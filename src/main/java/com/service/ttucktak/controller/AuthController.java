package com.service.ttucktak.controller;

import com.service.ttucktak.service.AuthService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auths")
public class AuthController {
    private final JwtUtility jwtUtility;

    private final RegexUtility regexUtility;

    private final AuthService authService;

    @Autowired
    public AuthController(JwtUtility jwtUtility, RegexUtility regexUtility, AuthService authService){
        this.jwtUtility = jwtUtility;
        this.regexUtility = regexUtility;
        this.authService = authService;
    }
}
