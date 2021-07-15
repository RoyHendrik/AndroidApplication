package com.pkg.to_day.ui.auth;

import com.pkg.to_day.ui.auth.login.LoginValidator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class LoginInputValidatorTest {

    static class LoginClass implements LoginValidator {
        public LoginClass() {
        }
    }

    LoginClass loginClass;

    @Before
    public void setUp() {
        this.loginClass = new LoginClass();
    }

    @Test
    public void test_email_invalid_variant_1() {
        String email = "lorem@ipsum";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_2() {
        String email = "lorem@.com";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_3() {
        String email = "@.com";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_invalid_variant_4() {
        String email = "@com";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_empty() {
        String email = "";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_to_short() {
        String email = "lorem@ipsum.com";
        String password = "pass";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_to_long() {
        String email = "lorem@ipsum.com";
        String password = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_to_short() {
        String email = "a@a";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_to_long() {
        String email = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@a.com";
        String password = "secretpassword";


        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_password_empty() {
        String email = "lorem@ipsum.com";
        String password = "";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_empty_and_password_empty() {
        String email = "";
        String password = "";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() > 0);
    }

    @Test
    public void test_email_correct_password_correct() {
        String email = "lorem@ipsum.com";
        String password = "secretpassword";

        ArrayList<String> validationErrors = loginClass.validateLogin(email, password);
        assertTrue(validationErrors.size() < 1);
    }

    @After
    public void tearDown() {
        this.loginClass = null;
    }
}
