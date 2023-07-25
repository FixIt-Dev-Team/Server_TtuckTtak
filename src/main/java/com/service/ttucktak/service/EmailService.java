package com.service.ttucktak.service;

import com.service.ttucktak.base.BaseException;
import com.service.ttucktak.dto.auth.PostEmailConfirmResDto;

public interface EmailService {
    PostEmailConfirmResDto sendSimpleMessage(String to) throws BaseException;
}
