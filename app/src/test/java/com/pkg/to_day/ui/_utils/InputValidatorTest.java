package com.pkg.to_day.ui._utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InputValidatorTest {

    @Test
    public void test_valid_email_variant_1() {
        String email = "lorem@ipsum.com";
        assertTrue(InputValidator.validateEmail(email));
    }

    @Test
    public void test_valid_email_variant_2() {
        String email = "lorem@.com";

        assertFalse(InputValidator.validateEmail(email));
    }

    @Test
    public void test_valid_email_variant_3() {
        String email = "@.com";

        assertFalse(InputValidator.validateEmail(email));
    }

    @Test
    public void test_valid_email_variant_4() {
        String email = "@com";

        assertFalse(InputValidator.validateEmail(email));
    }

    @Test
    public void test_equals_variant_1() {
        String s1 = "";
        String s2 = "-";

        assertFalse(InputValidator.validateEquals(s1, s2));
    }

    @Test
    public void test_equals_variant_2() {
        String s1 = "";
        String s2 = null;

        assertFalse(InputValidator.validateEquals(s1, s2));
    }

    @Test
    public void test_equals_variant_3() {
        String s1 = "lorem";
        String s2 = "lorem";

        assertTrue(InputValidator.validateEquals(s1, s2));
    }

    @Test
    public void test_valid_length_variant_1() {
        String input = "lorem";

        assertTrue(InputValidator.validateLength(input, 3, 10));
    }


    @Test
    public void test_valid_length_variant_2() {
        String input = "lorem ipsum";

        assertFalse(InputValidator.validateLength(input, 3, 10));
    }


    @Test
    public void test_valid_length_variant_3() {
        String input = "lorem ipsum";

        assertFalse(InputValidator.validateLength(input, 20, 100));
    }

    @Test
    public void test_empty_variant_1() {
        String input = "lorem ipsum";

        assertTrue(InputValidator.validateEmpty(input));
    }

    @Test
    public void test_empty_variant_2() {
        String input = "";

        assertFalse(InputValidator.validateEmpty(input));
    }


    @Test
    public void test_empty_variant_3() {
        String input = null;

        assertFalse(InputValidator.validateEmpty(input));
    }
}