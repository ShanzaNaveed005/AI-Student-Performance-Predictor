package com.example.aistudentperformancepredictor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudyMaterialActivity extends AppCompatActivity {

    private CardView cardRecommendedVideo, cardRecommendedNotes;
    private TextView tvRecommendationTitle, tvSubtext;
    private String weakestSubject = "General Study";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);

        // Views Initialize
        cardRecommendedVideo = findViewById(R.id.cardRecommendedVideo);
        cardRecommendedNotes = findViewById(R.id.cardRecommendedNotes);
        tvRecommendationTitle = findViewById(R.id.tvRecommendationTitle);
        tvSubtext = findViewById(R.id.tvSubtext);

        // AI Logic: Firebase se weak subject dhoondna
        fetchWeakestSubject();

        // Video Link Click
        cardRecommendedVideo.setOnClickListener(v -> {
            String url = "https://www.youtube.com/results?search_query=" + weakestSubject + "+tutorial+for+students";
            openUrl(url);
        });

        // Notes Link Click
        cardRecommendedNotes.setOnClickListener(v -> {
            String url = "https://www.google.com/search?q=" + weakestSubject + "+study+notes+pdf+filetype:pdf";
            openUrl(url);
        });
    }

    private void fetchWeakestSubject() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("StudentMarks").child(userId).child("subjects");

        db.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                double lowestPerc = 101;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    Long ob = ds.child("obtained").getValue(Long.class);
                    Long tt = ds.child("total").getValue(Long.class);

                    if (name != null && ob != null && tt != null) {
                        double p = (ob * 100.0) / tt;
                        if (p < lowestPerc) {
                            lowestPerc = p;
                            weakestSubject = name;
                        }
                    }
                }
                // UI Update karein AI ke mutabiq
                tvRecommendationTitle.setText("AI Recommendation: " + weakestSubject);
                tvSubtext.setText("Based on your performance, we suggest focusing on " + weakestSubject);
            }
        });
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i);
    }
}