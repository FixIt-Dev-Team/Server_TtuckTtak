package com.service.ttucktak.utils;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexUtil {

    public static boolean isValidEmail(String target) {
        String regex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9+-_.]+\\.[a-zA-Z0-9+-_.]+$";
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
