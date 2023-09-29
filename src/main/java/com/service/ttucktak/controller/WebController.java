package com.service.ttucktak.controller;

import com.service.ttucktak.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class WebController {

    /**
     * 타임리프 ttukttak 공식 페이지 로드
     * */
    @GetMapping()
    public String mainPage(){
        return "index";
    }

    @GetMapping("/index")
    public String indexPage(){
        return "index";
    }

    @GetMapping("/contact")
    public String contactPage(){
        return "contact";
    }
}
