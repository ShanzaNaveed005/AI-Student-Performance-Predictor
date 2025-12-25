package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Button click listener
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase login
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Login successful! Firebase connected.", Toast.LENGTH_SHORT).show();
                            Log.d("FirebaseTest","Login successful: " + email);

                            // TODO: Navigate to HomeActivity or Dashboard
                            // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            // finish();
                        } else {
                            Toast.makeText(this, "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("FirebaseTest","Login failed: " + task.getException().getMessage());
                        }
                    });
        });
    }
}
