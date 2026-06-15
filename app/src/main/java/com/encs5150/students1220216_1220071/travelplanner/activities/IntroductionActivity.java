package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.network.ConnectionAsyncTask;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class IntroductionActivity extends AppCompatActivity {

    private static final int MIN_REQUIRED_IMPORTED_TRIPS = 10;

    private MaterialButton connectButton;
    private ProgressBar connectProgress;
    private TextView connectStatus;
    private boolean connectInProgress;
    private ConnectionAsyncTask connectionTask;

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

        connectButton.setOnClickListener(v -> startConnectImport());
    }

    private void startConnectImport() {
        if (connectInProgress) {
            return;
        }

        setConnectLoading(true);
        connectStatus.setText(R.string.connect_loading_status);
        connectionTask = new ConnectionAsyncTask(this);
        connectionTask.execute();
    }

    public void onTripsFetched(List<Trip> trips) {
        connectionTask = null;

        if (trips == null || trips.size() < MIN_REQUIRED_IMPORTED_TRIPS) {
            showConnectFailure(R.string.connect_failure_status, R.string.connect_failure_toast);
            return;
        }

        try {
            TripRepository tripRepository = new TripRepository(this);
            tripRepository.importTrips(trips);

            int storedTripCount = tripRepository.getAllTrips().size();
            if (storedTripCount < MIN_REQUIRED_IMPORTED_TRIPS) {
                showConnectFailure(R.string.connect_invalid_data_status, R.string.connect_failure_toast);
                return;
            }

            connectStatus.setText(R.string.connect_success_status);
            Toast.makeText(this, R.string.connect_success_toast, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            showConnectFailure(R.string.connect_failure_status, R.string.connect_failure_toast);
        }
    }

    private void showConnectFailure(int statusMessageResId, int toastMessageResId) {
        setConnectLoading(false);
        connectStatus.setText(statusMessageResId);
        Toast.makeText(this, toastMessageResId, Toast.LENGTH_LONG).show();
    }

    private void setConnectLoading(boolean loading) {
        connectInProgress = loading;
        connectButton.setEnabled(!loading);
        connectProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (connectionTask != null) {
            connectionTask.cancel(true);
        }
        super.onDestroy();
    }
}
