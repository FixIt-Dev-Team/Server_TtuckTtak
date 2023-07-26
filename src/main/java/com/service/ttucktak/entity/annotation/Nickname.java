package com.service.ttucktak.entity.annotation;

import com.service.ttucktak.entity.validator.NicknameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NicknameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nickname {
    String message() default "올바른 형식의 닉네임여야 합니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}