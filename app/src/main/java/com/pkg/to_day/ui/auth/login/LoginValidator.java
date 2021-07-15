package com.pkg.to_day.ui.auth.login;

import com.pkg.to_day.ui._utils.InputValidator;

import java.util.ArrayList;

public interface LoginValidator {

    default ArrayList<String> validateLogin(String email, String password) {

        ArrayList<String> errorCollection = new ArrayList<>();

        if (!InputValidator.validateEmpty(email) || !InputValidator.validateEmpty(password))
            errorCollection.add("Not all fields are filled");
        else if (!InputValidator.validateEmail(email))
            errorCollection.add("Email is not correct");
        else if (!InputValidator.validateLength(email, 4, 255))
            errorCollection.add("Email has to be between 4 and 255 characters");
        else if (!InputValidator.validateLength(password, 6, 255))
            errorCollection.add("Password has to be between 6 and 255 characters");

        return errorCollection;
    }
}

