package com.service.ttucktak.utils;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexUtil {

    public static boolean isValidEmailFormat(String target) {
        String regex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9+-_.]+\\.[a-zA-Z0-9+-_.]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);

        return matcher.matches();
    }

    public static boolean isValidPwFormat(String target) {
        // 영문 대소문자, 숫자 특수문자 최소 한 개씩 포함 9자 이상 500자 이하
        String regex = "^(?=.?[A-Z])(?=.?[a-z])(?=.?[0-9])(?=.?[#?!@$ %^&*-]).{9,500}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);

        return matcher.matches();
    }

    public static boolean isValidNicknameFormat(String target) {
        // 4글자 이상, 12글자 미만
        String regex = ".{4,11}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);

        return matcher.matches();
    }

    public static boolean isValidDateFormat(Date target) {
        String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

        Pattern pattern = Pattern.compile(regex);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Matcher matcher = pattern.matcher(format.format(target));

        return matcher.matches();
    }
}
