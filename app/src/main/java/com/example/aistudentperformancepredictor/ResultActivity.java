package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvGrade, tvPercentage, tvWeakSubjects, tvAttendanceImpact;
    private BarChart barChart;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // UI Binding
        tvGrade = findViewById(R.id.tvGrade);
        tvPercentage = findViewById(R.id.tvPercentage);
        tvWeakSubjects = findViewById(R.id.tvWeakSubjects);
        tvAttendanceImpact = findViewById(R.id.tvAttendanceImpact);
        barChart = findViewById(R.id.barChart);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();
        // Toolbar bind karein
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Ab ye lines kaam karengi
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("AI Analysis Report");
        }

        loadAllDataAndAnalyze();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Ye user ko pichli screen (Dashboard) par le jayega
        return true;
    }

    private void loadAllDataAndAnalyze() {
        String userId = mAuth.getCurrentUser().getUid();

        // STEP 1: Pehle Attendance fetch karein
        rootRef.child("AttendanceData").child(userId).get().addOnSuccessListener(attSnapshot -> {
            int attendance = 100; // Default agar record na ho
            if (attSnapshot.exists()) {
                attendance = attSnapshot.child("attendancePercentage").getValue(Integer.class);
            }
            final int studentAttendance = attendance;

            // STEP 2: Ab Marks fetch karein
            rootRef.child("StudentMarks").child(userId).child("subjects").get().addOnSuccessListener(marksSnapshot -> {
                if (marksSnapshot.exists()) {
                    List<ResultEngine.Subject> subjectsList = new ArrayList<>();
                    for (DataSnapshot ds : marksSnapshot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        Long ob = ds.child("obtained").getValue(Long.class);
                        Long tt = ds.child("total").getValue(Long.class);
                        if (name != null && ob != null && tt != null) {
                            subjectsList.add(new ResultEngine.Subject(name, ob.intValue(), tt.intValue()));
                        }
                    }

                    // STEP 3: AI Logic Apply karein
                    performAiFinalAnalysis(subjectsList, studentAttendance);
                    showChart(subjectsList);
                } else {
                    Toast.makeText(this, "No marks found. Add marks first!", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> Toast.makeText(this, "Firebase Error!", Toast.LENGTH_SHORT).show());
    }

    private void performAiFinalAnalysis(List<ResultEngine.Subject> subjects, int attendance) {
        ResultEngine.Result res = ResultEngine.calculateResult(subjects);
        double finalScore = res.percentage;
        String impact;

        // AI Logic: Agar attendance 75% se kam hai, to marks deduct honge
        if (attendance < 75) {
            finalScore = finalScore - 5.0; // 5% Penalty
            impact = "⚠️ AI Warning: Grade penalized by 5% due to low attendance (" + attendance + "%).";
            tvAttendanceImpact.setTextColor(0xFFFF0000); // Red
        } else {
            impact = "✅ AI Status: Strong attendance (" + attendance + "%) is maintaining your grade.";
            tvAttendanceImpact.setTextColor(0xFF22C55E); // Green
        }

        tvGrade.setText("Predicted Grade: " + res.grade);
        tvPercentage.setText("AI Score Prediction: " + String.format("%.2f", finalScore) + "%");
        tvAttendanceImpact.setText(impact);

        if (!res.weakSubjects.isEmpty()) {
            StringBuilder sb = new StringBuilder("AI Priority Focus:\n");
            for (String s : res.weakSubjects) sb.append("• ").append(s).append("\n");
            tvWeakSubjects.setText(sb.toString());
        } else {
            tvWeakSubjects.setText("AI Insight: Performance is consistent across all subjects.");
        }
    }

    private void showChart(List<ResultEngine.Subject> subjects) {
        List<BarEntry> entries = new ArrayList<>();
        final List<String> labels = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            entries.add(new BarEntry(i, subjects.get(i).obtained));
            labels.add(subjects.get(i).name);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Marks Analysis");
        dataSet.setColor(0xFF7458AB); // Purple Theme
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int idx = (int) value;
                return (idx >= 0 && idx < labels.size()) ? labels.get(idx) : "";
            }
        });

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}