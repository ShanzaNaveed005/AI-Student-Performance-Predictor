package com.example.aistudentperformancepredictor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, LoginActivity.class)));
        btnSignup.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SignupActivity.class)));
    }
}
