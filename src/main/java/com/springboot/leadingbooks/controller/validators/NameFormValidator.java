package com.springboot.leadingbooks.controller.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface NameFormValidator {

    default boolean checkName(String name) {
        String regex = "^[가~힣]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
}
