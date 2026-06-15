package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DURATION_MS = 2500L;

    private final Handler splashHandler = new Handler(Looper.getMainLooper());
    private final Runnable openIntroductionRunnable = () -> {
        startActivity(new Intent(this, IntroductionActivity.class));
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View splashRoot = findViewById(R.id.splash_root);
        View logoView = findViewById(R.id.splash_logo);
        ViewCompat.setOnApplyWindowInsetsListener(splashRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_enter);
        logoView.startAnimation(logoAnimation);
        splashHandler.postDelayed(openIntroductionRunnable, SPLASH_DURATION_MS);
    }

    @Override
    protected void onDestroy() {
        splashHandler.removeCallbacks(openIntroductionRunnable);
        super.onDestroy();
    }
}
