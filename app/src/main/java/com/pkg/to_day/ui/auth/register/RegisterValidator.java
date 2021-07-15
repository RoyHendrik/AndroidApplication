package com.pkg.to_day.ui.auth.register;

import com.pkg.to_day.ui._utils.InputValidator;

import java.util.ArrayList;

public interface RegisterValidator {

    default ArrayList<String> validateRegister(String email, String password, String passwordRepeat) {
        ArrayList<String> errorCollection = new ArrayList<>();

        if (!InputValidator.validateEmpty(email) || !InputValidator.validateEmpty(password) || !InputValidator.validateEmpty(passwordRepeat))
            errorCollection.add("Not all fields are filled");
        else if (!InputValidator.validateEmail(email))
            errorCollection.add("Email is not correct");
        else if (!InputValidator.validateEquals(password, passwordRepeat))
            errorCollection.add("Passwords do not match");
        else if (!InputValidator.validateLength(email, 4, 255))
            errorCollection.add("Email has to be between 4 and 255 characters");
        else if (!InputValidator.validateLength(password, 6, 255))
            errorCollection.add("Password has to be between 6 and 255 characters");
        else if (!InputValidator.validateLength(passwordRepeat, 6, 255))
            errorCollection.add("Password has to be between 6 and 255 characters");

        return errorCollection;
    }
}
