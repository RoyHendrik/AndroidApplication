package com.pkg.to_day.ui.todo;

import com.pkg.to_day.ui._utils.InputValidator;

import java.util.ArrayList;

public interface TodoValidator {

    default ArrayList<String> validateTodo( String title, String context, String dueDate) {

        ArrayList<String> errorCollection = new ArrayList<>();

        if (!InputValidator.validateEmpty(title) || !InputValidator.validateEmpty(context) || !InputValidator.validateEmpty(dueDate))
            errorCollection.add("Not all fields are filled");
        else if (!InputValidator.validateLength(title, 3, 16))
            errorCollection.add("Title has to be between 3 and 16 characters");
        else if (!InputValidator.validateLength(context, 3, 255))
            errorCollection.add("Context has to be between 3 and 255 characters");
        else if (!InputValidator.validateDate(dueDate))
            errorCollection.add("Date is not correct, expected dd-MM-YYYY HH:mm");

        return errorCollection;
    }
}
