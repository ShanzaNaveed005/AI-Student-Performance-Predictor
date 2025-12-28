package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {

    private RecyclerView rvTimetable;
    private ProgressBar progressBar;
    private TextView tvStatus;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        rvTimetable = findViewById(R.id.rvTimetable);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        rvTimetable.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("StudentMarks");

        generateAiTimetable();
    }

    private void generateAiTimetable() {
        String userId = mAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        dbRef.child(userId).child("subjects").get().addOnSuccessListener(snapshot -> {
            progressBar.setVisibility(View.GONE);
            if (snapshot.exists()) {
                List<ResultEngine.Subject> subjects = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    Long obtained = ds.child("obtained").getValue(Long.class);
                    Long total = ds.child("total").getValue(Long.class);
                    if (name != null && obtained != null && total != null) {
                        subjects.add(new ResultEngine.Subject(name, obtained.intValue(), total.intValue()));
                    }
                }

                // AI Logic: Sort subjects by lowest percentage first
                Collections.sort(subjects, (s1, s2) -> {
                    double p1 = (s1.obtained * 100.0) / s1.total;
                    double p2 = (s2.obtained * 100.0) / s2.total;
                    return Double.compare(p1, p2);
                });

                buildSchedule(subjects);
            } else {
                tvStatus.setText("No marks found. Please add marks first to generate AI schedule.");
            }
        });
    }

    private void buildSchedule(List<ResultEngine.Subject> subjects) {
        List<ScheduleItem> schedule = new ArrayList<>();
        String[] slots = {"09:00 AM", "11:00 AM", "02:00 PM", "04:00 PM", "07:00 PM"};

        int subIndex = 0;
        for (int i = 0; i < slots.length; i++) {
            if (subIndex < subjects.size()) {
                ResultEngine.Subject s = subjects.get(subIndex);
                double perc = (s.obtained * 100.0) / s.total;

                String task = (perc < 50) ? "Intensive Study: " + s.name : "Revision: " + s.name;
                String duration = (perc < 50) ? "2 Hours" : "1 Hour";

                schedule.add(new ScheduleItem(slots[i], task, duration, perc < 50));
                subIndex++;
            } else {
                // Agar subjects khatam ho jayein to cycle dobara start karein (weakest first)
                subIndex = 0;
            }
        }
        // Adapter set karein (Adapter niche diya gaya hai)
        TimetableAdapter adapter = new TimetableAdapter(schedule);
        rvTimetable.setAdapter(adapter);
    }
}