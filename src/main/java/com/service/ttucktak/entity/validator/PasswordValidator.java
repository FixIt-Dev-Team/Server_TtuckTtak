package com.service.ttucktak.entity.validator;

import com.service.ttucktak.entity.annotation.Password;
import com.service.ttucktak.utils.RegexUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String pw, ConstraintValidatorContext context) {
        return RegexUtil.isValidPwFormat(pw);
    }
}