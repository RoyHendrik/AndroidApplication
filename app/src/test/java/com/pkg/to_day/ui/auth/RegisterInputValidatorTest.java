package com.pkg.to_day.ui.auth;

import com.pkg.to_day.ui.auth.register.RegisterValidator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class RegisterInputValidatorTest {

    static class RegisterClass implements RegisterValidator {
        public RegisterClass() {
        }
    }

    RegisterClass registerClass;

    @Before
    public void setUp() {
        this.registerClass = new RegisterClass();
    }


    @Test
    public void test_email_invalid_variant_1() {
        String email = "lorem@ipsum";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_2() {
        String email = "lorem@.com";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_3() {
        String email = "@.com";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_4() {
        String email = "@com";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_empty() {
        String email = "";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_to_short() {
        String email = "a@a";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_to_long() {
        String email = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@a.com";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_empty() {
        String email = "lorem@ipsum.com";
        String password = "";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_to_short() {
        String email = "lorem@ipsum.com";
        String password = "pass";
        String passwordRepeat = "pass";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_to_long() {
        String email = "lorem@ipsum.com";
        String password = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_repeat_empty() {
        String email = "lorem@ipsum.com";
        String password = "secretpassword";
        String passwordRepeat = "";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_repeat_to_short() {
        String email = "lorem@ipsum.com";
        String password = "pass";
        String passwordRepeat = "pass";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_repeat_to_long() {
        String email = "lorem@ipsum.com";
        String password = "secretpassword";
        String passwordRepeat = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_empty_and_password_empty() {
        String email = "";
        String password = "";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_empty_and_password_empty_and_password_repeat_empty() {
        String email = "";
        String password = "";
        String passwordRepeat = "";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_valid_password_valid_repeat_password_valid() {
        String email = "lorem@ipsum.com";
        String password = "secretpassword";
        String passwordRepeat = "secretpassword";

        ArrayList<String> validationErrors = registerClass.validateRegister(email, password, passwordRepeat);
        assertTrue(validationErrors.size() < 1);
    }

    @After
    public void tearDown() {
        this.registerClass = null;
    }
}
