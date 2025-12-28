package com.example.aistudentperformancepredictor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    private CardView cardAddMarks, cardPredict, cardTimetable, cardResources,cardChatbot;
    private TextView tvWelcome;
    private Button btnLogout;
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
        cardResources = findViewById(R.id.cardResources);
        cardChatbot = findViewById(R.id.cardChatbot);// XML mein id: cardResources lazmi honi chahiye
        btnLogout = findViewById(R.id.btnLogout);


        // 3. Load User Profile Name
        loadUserProfile();

        // 4. Click Listeners

        // Add Marks Card
        if (cardAddMarks != null) {
            cardAddMarks.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, StudentInputActivity.class);
                startActivity(intent);
            });
        }

        // Predict / Result Card
        if (cardPredict != null) {
            cardPredict.setOnClickListener(v -> {
                // Note: Agar aapki activity ka naam ResultActivity hai to ye theek hai
                Intent intent = new Intent(DashboardActivity.this, AttendanceAiActivity.class);
                startActivity(intent);
            });
        }

        // Timetable Card
        if (cardTimetable != null) {
            cardTimetable.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, TimetableActivity.class);
                startActivity(intent);
            });
        }

        // Study Material Card
        if (cardResources != null) {
            cardResources.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, StudyMaterialActivity.class);
                startActivity(intent);
            });
        }
        if (cardChatbot != null) {
            cardChatbot.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, ChatbotActivity.class);
                startActivity(intent);
            });
        }

        // Logout Button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }
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
                    } else {
                        tvWelcome.setText("Hello, Student!");
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