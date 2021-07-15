package com.pkg.to_day.ui.label;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class LabelInputValidatorTest {

    static class LabelClass implements LabelValidator {
        public LabelClass() {
        }
    }

    LabelClass labelClass;

    @Before
    public void setUp() {
        this.labelClass = new LabelClass();
    }

    @Test
    public void test_title_empty() {
        String title = "";

        ArrayList<String> validationErrors = labelClass.validateLabel(title);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_to_short() {
        String title = "1";

        ArrayList<String> validationErrors = labelClass.validateLabel(title);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_title_to_long() {
        String title = "Lorem ipsum dolor sum amit";

        ArrayList<String> validationErrors = labelClass.validateLabel(title);
        assertTrue(validationErrors.size() > 0);
    }

    @After
    public void tearDown() {
        this.labelClass = null;
    }
}