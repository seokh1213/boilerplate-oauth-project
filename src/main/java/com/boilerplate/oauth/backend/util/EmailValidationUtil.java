package com.boilerplate.oauth.backend.util;

import java.util.regex.Pattern;

public class EmailValidationUtil {
    public static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public static boolean validateEmail(String email) {
        return emailPattern.matcher(email).matches();
    }
}
