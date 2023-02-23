package com.service.ttucktak.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class jwtUtility {
    @Value("${jwt.secret-key}")
    private String jwtKey;
}
