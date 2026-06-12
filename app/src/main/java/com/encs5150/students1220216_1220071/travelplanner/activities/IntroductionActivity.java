package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.google.android.material.button.MaterialButton;

public class IntroductionActivity extends AppCompatActivity {

    private static final long TEMPORARY_CONNECT_DELAY_MS = 1200L;

    private final Handler connectHandler = new Handler(Looper.getMainLooper());
    private MaterialButton connectButton;
    private ProgressBar connectProgress;
    private TextView connectStatus;
    private boolean connectInProgress;

    private final Runnable temporaryConnectResult = () -> {
        connectInProgress = false;
        connectProgress.setVisibility(View.GONE);
        connectButton.setEnabled(true);
        connectStatus.setText(R.string.connect_not_implemented_status);
        Toast.makeText(this, R.string.connect_not_implemented_toast, Toast.LENGTH_LONG).show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        View introductionRoot = findViewById(R.id.introduction_root);
        ViewCompat.setOnApplyWindowInsetsListener(introductionRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        connectButton = findViewById(R.id.connect_button);
        connectProgress = findViewById(R.id.connect_progress);
        connectStatus = findViewById(R.id.connect_status);

        connectButton.setOnClickListener(v -> startTemporaryConnect());
    }

    private void startTemporaryConnect() {
        if (connectInProgress) {
            return;
        }

        connectInProgress = true;
        connectButton.setEnabled(false);
        connectProgress.setVisibility(View.VISIBLE);
        connectStatus.setText(R.string.connect_loading_status);
        connectHandler.postDelayed(temporaryConnectResult, TEMPORARY_CONNECT_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        connectHandler.removeCallbacks(temporaryConnectResult);
        super.onDestroy();
    }
}
