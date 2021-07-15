package com.pkg.to_day.ui.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pkg.to_day.R;
import com.pkg.to_day.dao.UserDao;
import com.pkg.to_day.database.AppDatabase;
import com.pkg.to_day.models.User;
import com.pkg.to_day.ui.auth.login.LoginActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements RegisterValidator {

    private EditText txtEmail, txtPassword, txtPasswordRepeat;
    private Button btnRegister, btnBackToLogin;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = findViewById(R.id.act_register_input_email);
        txtPassword = findViewById(R.id.act_register_input_password);
        txtPasswordRepeat = findViewById(R.id.act_register_input_password_repeat);
        btnRegister = findViewById(R.id.act_register_button_register);
        btnBackToLogin = findViewById(R.id.act_register_button_to_login);
        userDao = AppDatabase.getAppDatabase(getApplicationContext()).userDao();

        initListeners();
    }

    private void initListeners() {
        btnRegister.setOnClickListener(v -> registerSubmitForm());
        btnBackToLogin.setOnClickListener(v -> transitionToLogin());
    }

    private void registerSubmitForm() {
        String email = txtEmail.getText().toString().toLowerCase();
        String password = txtPassword.getText().toString();
        String passwordRepeat = txtPasswordRepeat.getText().toString();

        ArrayList<String> validationErrors = validateRegister(email, password, passwordRepeat);

        if (validationErrors.size() == 0) {
            if (userDao.exists(email)) {
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show());
            } else {
                User user = new User(email, password);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                userDao.register(user);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show());
                startActivity(i);
            }
        } else {
            validationErrors.forEach(error ->
                    this.runOnUiThread(() -> Toast.makeText(this.getApplicationContext(), error, Toast.LENGTH_LONG).show()));
        }
    }

    private void transitionToLogin() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }
}