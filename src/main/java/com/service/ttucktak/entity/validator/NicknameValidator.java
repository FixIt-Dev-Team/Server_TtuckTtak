package com.service.ttucktak.entity.validator;

import com.service.ttucktak.entity.annotation.Nickname;
import com.service.ttucktak.service.ProfanityFilterService;
import com.service.ttucktak.utils.RegexUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NicknameValidator implements ConstraintValidator<Nickname, String> {

    private final ProfanityFilterService profanityFilterService;

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        return RegexUtil.isValidNicknameFormat(nickname) && !profanityFilterService.containsProfanity(nickname);
    }
}
