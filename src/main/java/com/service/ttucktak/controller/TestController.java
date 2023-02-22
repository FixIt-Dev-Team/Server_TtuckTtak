package com.service.ttucktak.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/")
    public ResponseEntity<String> testApi(){
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
