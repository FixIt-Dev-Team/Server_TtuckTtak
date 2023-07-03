package com.service.ttucktak.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check")
public class TestController {

    @GetMapping("/health")
    public String healthTest(){
        return "OK";
    }
}
