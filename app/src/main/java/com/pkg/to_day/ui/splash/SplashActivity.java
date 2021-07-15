package com.pkg.to_day.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pkg.to_day.R;
import com.pkg.to_day.ui.auth.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        buttonStart = findViewById(R.id.act_splash_button_start);

        initListeners();
    }

    private void initListeners() {
        buttonStart.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }
}
