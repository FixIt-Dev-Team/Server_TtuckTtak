package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseException;

public interface EmailService {
    String sendSimpleMessage(String to) throws BaseException;
}
