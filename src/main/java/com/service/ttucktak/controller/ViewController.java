package com.service.ttucktak.controller;

import com.service.ttucktak.service.ViewService;
import com.service.ttucktak.utils.JwtUtil;
import com.service.ttucktak.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/views")
public class ViewController {
    private final JwtUtil jwtUtil;

    private final RegexUtil regexUtil;

    private final ViewService viewService;

    @Autowired
    public ViewController(JwtUtil jwtUtil, RegexUtil regexUtil, ViewService viewService){
        this.jwtUtil = jwtUtil;
        this.regexUtil = regexUtil;
        this.viewService = viewService;
    }
}
