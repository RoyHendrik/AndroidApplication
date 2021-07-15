package com.pkg.to_day.ui.label;

import com.pkg.to_day.ui._utils.InputValidator;

import java.util.ArrayList;

public interface LabelValidator {

    default ArrayList<String> validateLabel(String title) {

        ArrayList<String> errorCollection = new ArrayList<>();

        if (!InputValidator.validateEmpty(title))
            errorCollection.add("Not all fields are filled");
        else if (!InputValidator.validateLength(title, 2, 8))
            errorCollection.add("Label has to be between 2 and 8 characters");

        return errorCollection;
    }
}
