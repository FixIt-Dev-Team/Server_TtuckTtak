package com.service.ttucktak.controller;

import com.service.ttucktak.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class WebController {

    /**
     * 타임리프 ttukttak 공식 페이지 로드
     * */
    @GetMapping()
    public String mainPage(){
        return "/index.html";
    }

    @GetMapping("/index")
    public String indexPage(){
        return "/index.html";
    }

    @GetMapping("/contact")
    public String contactPage(){
        return "/contact.html";
    }
}
