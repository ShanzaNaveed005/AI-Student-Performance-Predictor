package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentInputActivity extends AppCompatActivity {

    private LinearLayout subjectsContainer;
    private Button btnAddSubject, btnSubmitData;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private List<EditText[]> subjectRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_input);

        subjectsContainer = findViewById(R.id.subjectsContainer);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnSubmitData = findViewById(R.id.btnSubmitData);

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("StudentMarks");

        subjectRows = new ArrayList<>();

        btnAddSubject.setOnClickListener(v -> addSubjectRow());
        btnSubmitData.setOnClickListener(v -> submitData());

        // Add first row by default
        addSubjectRow();

        // Toolbar bind karein
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Ab ye lines kaam karengi
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void addSubjectRow() {
        LinearLayout row = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.item_subject_row, subjectsContainer, false);

        EditText etName = row.findViewById(R.id.etSubject);
        EditText etObtained = row.findViewById(R.id.etObtained);
        EditText etTotal = row.findViewById(R.id.etTotal);

        subjectsContainer.addView(row);
        subjectRows.add(new EditText[]{etName, etObtained, etTotal});
    }

    private void submitData() {
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> subjectsMap = new HashMap<>();
        for (int i = 0; i < subjectRows.size(); i++) {
            EditText[] row = subjectRows.get(i);
            String name = row[0].getText().toString().trim();
            String obtainedStr = row[1].getText().toString().trim();
            String totalStr = row[2].getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(obtainedStr) || TextUtils.isEmpty(totalStr)) {
                Toast.makeText(this, "Fill all fields for all subjects", Toast.LENGTH_SHORT).show();
                return;
            }

            int obtained = Integer.parseInt(obtainedStr);
            int total = Integer.parseInt(totalStr);

            Map<String, Object> sub = new HashMap<>();
            sub.put("name", name);
            sub.put("obtained", obtained);
            sub.put("total", total);

            subjectsMap.put("subject" + i, sub);
        }

        dbRef.child(userId).child("subjects").setValue(subjectsMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Firebase error: "+e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
