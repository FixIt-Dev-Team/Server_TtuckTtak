package com.service.ttucktak.controller;

import com.service.ttucktak.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/page/leafs")
public class LeafController {

    private final JwtUtil jwtUtil;

    @Autowired
    public LeafController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 타임리프 비밀번호 변경 페이지 API
     * */
    @GetMapping("/password")
    public String passwordPage(Model model, @RequestParam("email") String email){
        model.addAttribute("email", email);
        return "/modify-password";
    }
}
