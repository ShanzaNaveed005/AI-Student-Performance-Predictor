package com.example.aistudentperformancepredictor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    // Splash ka total time 3.5 seconds
    private static final int SPLASH_DELAY = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Layout set karna sabse pehla kaam hai
        setContentView(R.layout.activity_splash);

        // 2. Firebase initialize (Aapke connection ko nahi chhera)
        FirebaseApp.initializeApp(this);

        // 3. Views find karna (Check karein ke XML mein IDs yahi hain)
        ImageView logo = findViewById(R.id.logoImage);
        TextView title = findViewById(R.id.tvAppName);
        TextView tagline = findViewById(R.id.tvTagline);

        // Safety Check: Agar XML mein koi ID ghalat hai to app crash na ho
        if (logo != null && title != null && tagline != null) {

            // Logo Animation: Fade in
            logo.setAlpha(0f);
            logo.animate().alpha(1f).setDuration(1500).start();

            // Title Animation: Slide up and Fade in
            title.setTranslationY(50f);
            title.setAlpha(0f);
            title.animate().translationY(0f).alpha(1f).setDuration(1200).setStartDelay(500).start();

            // Tagline Animation: Fade in late
            tagline.setAlpha(0f);
            tagline.animate().alpha(1f).setDuration(1000).setStartDelay(1000).start();
        }

        // 4. Agli activity par jana (Handler with Main Looper for stability)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Yahan check karein ke HomeActivity ki class sahi hai
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);

            // Finish taake user back button daba kar wapis splash par na aaye
            finish();
        }, SPLASH_DELAY);
    }
}