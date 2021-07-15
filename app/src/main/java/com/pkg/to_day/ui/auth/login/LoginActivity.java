package com.pkg.to_day.ui.auth.login;

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
import com.pkg.to_day.ui.MainActivity;
import com.pkg.to_day.ui.auth.register.RegisterActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements LoginValidator {

    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.act_login_input_email);
        txtPassword = findViewById(R.id.act_login_input_password);
        btnLogin = findViewById(R.id.act_login_button_login);
        btnRegister = findViewById(R.id.act_login_button_to_register);

        initListeners();
    }

    private void initListeners() {
        btnLogin.setOnClickListener(v -> submitLoginForm());
        btnRegister.setOnClickListener(v -> transitionToRegister());
    }

    private void submitLoginForm() {
        String email = txtEmail.getText().toString().toLowerCase();
        String password = txtPassword.getText().toString();

        ArrayList<String> validationErrors = validateLogin(email, password);

        if (validationErrors.size() == 0) {
            AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
            UserDao userDao = appDatabase.userDao();

            new Thread(() -> {
                User user = userDao.login(email, password);

                if (user == null) {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "This account does not exist!", Toast.LENGTH_SHORT).show());
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class)
                            .putExtra("email", email);
                    startActivity(i);
                }

            }).start();
        } else {
            validationErrors.forEach(error ->
                    this.runOnUiThread(() -> Toast.makeText(this.getApplicationContext(), error, Toast.LENGTH_LONG).show()));
        }
    }

    private void transitionToRegister() {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }
}

