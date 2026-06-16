package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.isAdmin(this)) {
            SessionManager.clearSession(this);
            Toast.makeText(this, R.string.admin_access_denied_toast, Toast.LENGTH_LONG).show();
            routeToLogin();
            return;
        }

        setContentView(R.layout.activity_admin_home);

        View adminRoot = findViewById(R.id.admin_home_root);
        ViewCompat.setOnApplyWindowInsetsListener(adminRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView sessionSummary = findViewById(R.id.admin_session_summary);
        sessionSummary.setText(getString(
                R.string.admin_home_session_summary,
                SessionManager.getCurrentUserEmail(this)
        ));
    }

    private void routeToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
