package com.encs5150.students1220216_1220071.travelplanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.encs5150.students1220216_1220071.travelplanner.activities.AdminHomeActivity;
import com.encs5150.students1220216_1220071.travelplanner.activities.LoginActivity;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.hasActiveSession(this)) {
            routeToLogin();
            return;
        }

        if (SessionManager.isAdmin(this)) {
            routeToAdminHome();
            return;
        }

        if (!SessionManager.isUser(this)) {
            SessionManager.clearSession(this);
            routeToLogin();
            return;
        }

        setContentView(R.layout.activity_main);
    }

    private void routeToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void routeToAdminHome() {
        Intent intent = new Intent(this, AdminHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
