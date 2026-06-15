package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private static final String AUTH_PREFS_NAME = "travel_planner_auth";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";
    private static final long TEMPORARY_LOGIN_DELAY_MS = 800L;

    private final Handler loginHandler = new Handler(Looper.getMainLooper());
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText emailInput;
    private MaterialCheckBox rememberMeCheckBox;
    private ProgressBar loginProgress;
    private MaterialButton loginButton;
    private MaterialButton signUpButton;
    private TextView loginStatus;
    private boolean loginInProgress;

    private final Runnable temporaryLoginResult = () -> {
        setLoginLoading(false);
        loginStatus.setText(R.string.login_auth_pending_status);
        Toast.makeText(this, R.string.login_auth_pending_toast, Toast.LENGTH_LONG).show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View loginRoot = findViewById(R.id.login_root);
        ViewCompat.setOnApplyWindowInsetsListener(loginRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInputLayout = findViewById(R.id.login_email_input_layout);
        passwordInputLayout = findViewById(R.id.login_password_input_layout);
        emailInput = findViewById(R.id.login_email_input);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        loginProgress = findViewById(R.id.login_progress);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        loginStatus = findViewById(R.id.login_status);

        prefillRememberedEmail();

        loginButton.setOnClickListener(v -> showTemporaryLoginLoading());
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void prefillRememberedEmail() {
        SharedPreferences preferences = getSharedPreferences(AUTH_PREFS_NAME, MODE_PRIVATE);
        String rememberedEmail = preferences.getString(KEY_REMEMBERED_EMAIL, "");
        emailInput.setText(rememberedEmail);
        rememberMeCheckBox.setChecked(!rememberedEmail.isEmpty());
    }

    private void showTemporaryLoginLoading() {
        if (loginInProgress) {
            return;
        }

        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        loginStatus.setText(R.string.login_loading_status);
        setLoginLoading(true);
        loginHandler.postDelayed(temporaryLoginResult, TEMPORARY_LOGIN_DELAY_MS);
    }

    private void setLoginLoading(boolean loading) {
        loginInProgress = loading;
        loginProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
        signUpButton.setEnabled(!loading);
    }

    @Override
    protected void onDestroy() {
        loginHandler.removeCallbacks(temporaryLoginResult);
        super.onDestroy();
    }
}
