package com.service.ttucktak.controller;

import com.service.ttucktak.service.ViewService;
import com.service.ttucktak.utils.JwtUtility;
import com.service.ttucktak.utils.RegexUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/views")
public class ViewController {
    private final JwtUtility jwtUtility;

    private final RegexUtility regexUtility;

    private final ViewService viewService;

    @Autowired
    public ViewController(JwtUtility jwtUtility, RegexUtility regexUtility, ViewService viewService){
        this.jwtUtility = jwtUtility;
        this.regexUtility = regexUtility;
        this.viewService = viewService;
    }
}
