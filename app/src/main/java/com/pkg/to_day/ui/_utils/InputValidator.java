package com.pkg.to_day.ui._utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public interface InputValidator {

    static Boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);

        return pat.matcher(email).matches();
    }

    static Boolean validateEquals(String s1, String s2) {

        return s1.equals(s2);
    }

    static Boolean validateLength(String input, Integer min, Integer max) {
        if (input.length() < min) return false;

        return input.length() <= max;
    }

    static Boolean validateDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm");
        simpleDateFormat.setLenient(false);

        Date parsedDate = null;
        try {
            parsedDate = simpleDateFormat.parse(date);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return parsedDate != null;
    }


    static boolean validateEmpty(String input) {

        if (input == null) return false;

        if (input.equals("")) return false;

        return input.length() >= 1;
    }
}
