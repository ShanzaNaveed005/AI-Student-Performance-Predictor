package com.example.aistudentperformancepredictor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// login

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            // TODO: Firebase Authentication
            Toast.makeText(this, "Login clicked (Firebase Auth integration later)", Toast.LENGTH_SHORT).show();
        });
    }
}
