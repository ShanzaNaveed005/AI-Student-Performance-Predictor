package com.example.aistudentperformancepredictor;

import android.content.Intent; // Ye import zaroori hai
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AttendanceAiActivity extends AppCompatActivity {

    private TextInputEditText etAttendance;
    private TextView tvRisk, tvSuggestion;
    private Button btnAnalyze;
    private ImageView imgStatus;

    private DatabaseReference attendanceRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_ai);

        mAuth = FirebaseAuth.getInstance();
        attendanceRef = FirebaseDatabase.getInstance().getReference("AttendanceData");

        etAttendance = findViewById(R.id.etAttendance);
        tvRisk = findViewById(R.id.tvRisk);
        tvSuggestion = findViewById(R.id.tvSuggestion);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        imgStatus = findViewById(R.id.imgStatus);

        btnAnalyze.setOnClickListener(v -> analyzeAttendance());
    }

    private void analyzeAttendance() {
        String input = etAttendance.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Enter percentage", Toast.LENGTH_SHORT).show();
            return;
        }

        int attendance = Integer.parseInt(input);
        if (attendance > 100) {
            Toast.makeText(this, "Percentage cannot exceed 100", Toast.LENGTH_SHORT).show();
            return;
        }

        String risk;
        String suggestion;
        int statusIcon;
        int statusColor;

        // ---------- ADVANCED AI LOGIC ----------
        if (attendance < 65) {
            risk = "CRITICAL RISK ❌";
            suggestion = "AI Prediction: High chance of failing. Your attendance is below the required threshold.";
            statusIcon = android.R.drawable.ic_delete;
            statusColor = 0xFFFF0000; // Red
        } else if (attendance < 85) {
            risk = "MODERATE RISK ⚠️";
            suggestion = "AI Prediction: Performance stable, but consistency is needed to secure an 'A'.";
            statusIcon = android.R.drawable.ic_dialog_alert;
            statusColor = 0xFFFFA500; // Orange
        } else {
            risk = "PERFORMANCE OPTIMIZED ✅";
            suggestion = "AI Prediction: Excellent! Your attendance increases 'A+' probability by 30%.";
            statusIcon = android.R.drawable.ic_input_add;
            statusColor = 0xFF22C55E; // Green
        }

        tvRisk.setText(risk);
        tvRisk.setTextColor(statusColor);
        tvSuggestion.setText(suggestion);
        imgStatus.setImageResource(statusIcon);
        imgStatus.setColorFilter(statusColor);

        saveToFirebase(attendance, risk, suggestion);
    }

    private void saveToFirebase(int attendance, String risk, String suggestion) {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();
        AttendanceModel model = new AttendanceModel(userId, attendance, risk, suggestion, System.currentTimeMillis());

        // Save data and then navigate to ResultActivity
        attendanceRef.child(userId).setValue(model)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AttendanceAiActivity.this, "AI Analysis Updated!", Toast.LENGTH_SHORT).show();

                    // Direct Result screen par le jayen analysis ke baad
                    Intent intent = new Intent(AttendanceAiActivity.this, ResultActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(AttendanceAiActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}