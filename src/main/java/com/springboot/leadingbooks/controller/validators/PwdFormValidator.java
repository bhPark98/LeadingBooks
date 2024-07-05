package com.springboot.leadingbooks.controller.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface PwdFormValidator {

    default boolean check_alphabet(String pwd) {
        String regex = "[a-z|A-Z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd);

        return matcher.find();
    }

    default boolean check_number(String pwd) {
        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd);

        return matcher.find();
    }

    default boolean check_special(String pwd) {
        String regex = "[!@#$%^&*()_+\\-=]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd);

        return matcher.find();
    }
}
