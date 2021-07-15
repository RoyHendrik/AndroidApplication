package com.pkg.to_day.ui.todo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class ToDoInputValidatorTest {

    static class ToDoClass implements TodoValidator {
        public ToDoClass() {
        }
    }

    ToDoClass todoClass;

    @Before
    public void setUp() {
        this.todoClass = new ToDoClass();
    }

    @Test
    public void test_title_empty_context_empty_duedate_empty() {
        String title = "";
        String context = "";
        String dueDate = "";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_valid_context_empty_duedate_empty() {
        String title = "Title";
        String context = "";
        String dueDate = "";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_valid_context_valid_duedate_empty() {
        String title = "Title";
        String context = "Lorem ipum";
        String dueDate = "";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_valid_context_valid_duedate_valid() {
        String title = "Title";
        String context = "Lorem ipum";
        String dueDate = "01-01-2020 00:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void test_title_to_short() {
        String title = "1";
        String context = "Lorem ipum";
        String dueDate = "01-01-2020 00:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_to_long() {
        String title = "Lorem ipsum dolor sum amit consector";
        String context = "Lorem ipum";
        String dueDate = "01-01-2020 00:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_context_to_short() {
        String title = "Title";
        String context = "1";
        String dueDate = "01-01-2020 00:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_context_to_long() {
        String title = "Title";
        String context = "Lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem " +
                "ipsum lorem ipsum";
        String dueDate = "01-01-2020 00:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_invalid_date_variant_1() {
        String title = "Title";
        String context = "Lorem ipum";
        String dueDate = "Lorem ipsum";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_invalid_date_variant_2() {
        String title = "Title";
        String context = "Lorem ipum";
        String dueDate = "01-01-2020";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_invalid_date_variant_3() {
        String title = "Title";
        String context = "Lorem ipum";
        String dueDate = "01-01-202001:00";

        ArrayList<String> validationErrors = todoClass.validateTodo(title, context, dueDate);
        assertTrue(validationErrors.size() > 0);
    }

    @After
    public void tearDown() {
        this.todoClass = null;
    }
}