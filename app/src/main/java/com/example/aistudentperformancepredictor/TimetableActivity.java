package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TimetableActivity extends AppCompatActivity {

    TextView tvTimetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        tvTimetable = findViewById(R.id.tvTimetable);

        String timetable =
                "Monday: Math – 2 hrs\n" +
                        "Tuesday: Physics – 2 hrs\n" +
                        "Wednesday: Weak Subject – 3 hrs\n" +
                        "Thursday: Revision – 2 hrs\n" +
                        "Friday: Practice Test\n" +
                        "Saturday: Doubt Clearing\n" +
                        "Sunday: Light Revision";

        tvTimetable.setText(timetable);
    }
}
