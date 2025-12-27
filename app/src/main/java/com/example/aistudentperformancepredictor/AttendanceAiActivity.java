package com.example.aistudentperformancepredictor;
import com.example.aistudentperformancepredictor.AttendanceModel;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AttendanceAiActivity extends AppCompatActivity {

    // UI components
    private TextInputEditText etAttendance;
    private TextView tvRisk, tvSuggestion;
    private Button btnAnalyze;

    // Firebase
    private DatabaseReference attendanceRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_ai);

        // ---------- Firebase ----------
        mAuth = FirebaseAuth.getInstance();
        attendanceRef = FirebaseDatabase.getInstance()
                .getReference("AttendanceAI");

        // ---------- UI Binding ----------
        etAttendance = findViewById(R.id.etAttendance);
        tvRisk = findViewById(R.id.tvRisk);
        tvSuggestion = findViewById(R.id.tvSuggestion);
        btnAnalyze = findViewById(R.id.btnAnalyze);

        // ---------- Button Click ----------
        btnAnalyze.setOnClickListener(v -> analyzeAttendance());
    }

    private void analyzeAttendance() {

        // Validation
        if (etAttendance.getText() == null ||
                etAttendance.getText().toString().trim().isEmpty()) {

            Toast.makeText(this,
                    "Please enter attendance percentage",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int attendance = Integer.parseInt(
                etAttendance.getText().toString().trim());

        String risk;
        String suggestion;

        // ---------- AI LOGIC (Rule-based) ----------
        if (attendance < 60) {
            risk = "High Risk ❌";
            suggestion = "Your attendance is critically low. Attend all remaining classes immediately.";
        } else if (attendance < 75) {
            risk = "Medium Risk ⚠️";
            suggestion = "Attendance is borderline. Improve consistency to avoid issues.";
        } else {
            risk = "Safe ✅";
            suggestion = "Excellent attendance. Keep maintaining this consistency.";
        }

        // ---------- Show Result on Screen ----------
        tvRisk.setText("Risk Level: " + risk);
        tvSuggestion.setText(suggestion);

        // ---------- Save to Firebase ----------
        saveAttendanceToFirebase(attendance, risk, suggestion);
    }

    private void saveAttendanceToFirebase(int attendance,
                                          String risk,
                                          String suggestion) {

        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid()
                : "anonymous";

        String recordId = attendanceRef.push().getKey();

        AttendanceModel model = new AttendanceModel(
                userId,
                attendance,
                risk,
                suggestion,
                System.currentTimeMillis()
        );

        if (recordId != null) {
            attendanceRef.child(recordId).setValue(model)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(this,
                                    "Attendance analysis saved",
                                    Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    "Firebase error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show());
        }
    }
}
