package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView; // Yeh add kiya hai
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword; // Yeh naya variable hai
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
        tvForgotPassword = findViewById(R.id.tvForgotPassword); // XML wali ID yahan connect ki

        // Login Button Click
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
                        } else {
                            Toast.makeText(this, "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("FirebaseTest","Login failed: " + task.getException().getMessage());
                        }
                    });
        });

        // --- Forgot Password Logic START ---
        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                // Agar user ne email nahi likha to pehle usse email mangenge
                Toast.makeText(this, "Please enter your email in the email field first", Toast.LENGTH_LONG).show();
                etEmail.setError("Email required for reset");
            } else {
                // Firebase Password Reset Email send karega
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Reset link sent to your email!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        // --- Forgot Password Logic END ---
    }
}