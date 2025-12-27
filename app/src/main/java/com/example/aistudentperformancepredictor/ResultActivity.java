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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvGrade, tvPercentage, tvWeakSubjects;
    private BarChart barChart;

    private FirebaseAuth mAuth;
    private DatabaseReference marksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvGrade = findViewById(R.id.tvGrade);
        tvPercentage = findViewById(R.id.tvPercentage);
        tvWeakSubjects = findViewById(R.id.tvWeakSubjects);
        barChart = findViewById(R.id.barChart);

        mAuth = FirebaseAuth.getInstance();
        marksRef = FirebaseDatabase.getInstance().getReference("StudentMarks");

        loadMarksFromFirebase();
    }

    private void loadMarksFromFirebase() {
        String userId = mAuth.getCurrentUser().getUid();

        marksRef.child(userId).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                List<ResultEngine.Subject> subjects = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    int obtained = ds.child("obtained").getValue(Integer.class);
                    int total = ds.child("total").getValue(Integer.class);
                    subjects.add(new ResultEngine.Subject(name, obtained, total));
                }

                ResultEngine.Result result = ResultEngine.calculateResult(subjects);

                tvGrade.setText("Grade: " + result.grade);
                tvPercentage.setText("Percentage: " + String.format("%.2f", result.percentage) + "%");

                if (result.weakSubjects.size() > 0) {
                    StringBuilder sb = new StringBuilder("Weak Subjects:\n");
                    for (String ws : result.weakSubjects) sb.append(ws).append("\n");
                    tvWeakSubjects.setText(sb.toString());
                } else tvWeakSubjects.setText("No weak subjects 🎉");

                showChart(subjects);

            } else {
                Toast.makeText(this, "No marks found for this user", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Firebase error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void showChart(List<ResultEngine.Subject> subjects) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;
        for (ResultEngine.Subject s : subjects) {
            entries.add(new BarEntry(index, s.obtained));
            labels.add(s.name);
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Subjects Marks");
        dataSet.setColor(getResources().getColor(R.color.black));
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index);
                } else {
                    return "";
                }
            }
        });

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setEnabled(false);
        barChart.setFitBars(true);
        barChart.invalidate();
    }
}
