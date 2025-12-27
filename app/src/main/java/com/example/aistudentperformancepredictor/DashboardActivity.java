package com.example.aistudentperformancepredictor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {

    private CardView cardAddMarks, cardPredict, cardTimetable, cardResources;
    private TextView tvWelcome;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // 2. Initialize Views
        tvWelcome = findViewById(R.id.tvWelcome);
        cardAddMarks = findViewById(R.id.cardAddMarks);
        cardPredict = findViewById(R.id.cardPredict);
        cardTimetable = findViewById(R.id.cardTimetable);
        // cardResources = findViewById(R.id.cardResources); // Agar XML mein ID di hai

        // 3. Load User Profile Name (Firebase se naam uthana)
        loadUserProfile();

        // 4. Click Listeners for High-Level Navigation
        cardAddMarks.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, StudentInputActivity.class);
            startActivity(intent);
        });

        cardPredict.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ResultActivity.class);
            startActivity(intent);
        });

        cardTimetable.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, TimetableActivity.class);
            startActivity(intent);
        });

        // Extra Card for Study Material (Placeholder)
        /*
        cardResources.setOnClickListener(v ->
            Toast.makeText(this, "Study Materials Coming Soon!", Toast.LENGTH_SHORT).show());
        */
    }

    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Database se 'name' fetch karna
            mDatabase.child(userId).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.getValue(String.class);
                        tvWelcome.setText("Hello, " + name + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvWelcome.setText("Hello, Student!");
                }
            });
        }
    }
}