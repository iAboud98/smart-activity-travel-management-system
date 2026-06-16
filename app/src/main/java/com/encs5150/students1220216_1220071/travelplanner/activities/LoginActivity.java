package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.MainActivity;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_PREFILL_EMAIL = "com.encs5150.travelplanner.PREFILL_EMAIL";

    private static final String AUTH_PREFS_NAME = "travel_planner_auth";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialCheckBox rememberMeCheckBox;
    private ProgressBar loginProgress;
    private MaterialButton loginButton;
    private MaterialButton signUpButton;
    private TextView loginStatus;
    private UserRepository userRepository;
    private boolean loginInProgress;

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
        passwordInput = findViewById(R.id.login_password_input);
        rememberMeCheckBox = findViewById(R.id.remember_me_checkbox);
        loginProgress = findViewById(R.id.login_progress);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        loginStatus = findViewById(R.id.login_status);
        userRepository = new UserRepository(this);

        prefillRememberedEmail();

        loginButton.setOnClickListener(v -> validateAndLogin());
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void prefillRememberedEmail() {
        String prefillEmail = getIntent().getStringExtra(EXTRA_PREFILL_EMAIL);
        if (!ValidationUtils.isBlank(prefillEmail)) {
            emailInput.setText(prefillEmail);
            rememberMeCheckBox.setChecked(false);
            return;
        }

        SharedPreferences preferences = getSharedPreferences(AUTH_PREFS_NAME, MODE_PRIVATE);
        String rememberedEmail = preferences.getString(KEY_REMEMBERED_EMAIL, "");
        emailInput.setText(rememberedEmail);
        rememberMeCheckBox.setChecked(!rememberedEmail.isEmpty());
    }

    private void validateAndLogin() {
        if (loginInProgress) {
            return;
        }

        if (!validateLoginForm()) {
            loginStatus.setText(R.string.login_validation_error_status);
            return;
        }

        loginStatus.setText(R.string.login_loading_status);
        setLoginLoading(true);
        String email = ValidationUtils.normalizeEmail(getText(emailInput));
        String password = getText(passwordInput);
        User user = userRepository.loginUser(email, password);
        setLoginLoading(false);

        if (user == null) {
            SessionManager.clearSession(this);
            loginStatus.setText(R.string.login_invalid_credentials_status);
            Toast.makeText(this, R.string.login_invalid_credentials_toast, Toast.LENGTH_LONG).show();
            return;
        }

        updateRememberedEmail(email);
        SessionManager.saveSession(this, user);
        routeAfterLogin(user);
    }

    private boolean validateLoginForm() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        String email = getText(emailInput);
        String password = getText(passwordInput);
        View firstInvalidView = null;
        boolean valid = true;

        if (ValidationUtils.isBlank(email)) {
            emailInputLayout.setError(getString(R.string.validation_email_required));
            firstInvalidView = emailInput;
            valid = false;
        } else if (!ValidationUtils.isValidEmail(email)) {
            emailInputLayout.setError(getString(R.string.validation_email_format));
            firstInvalidView = emailInput;
            valid = false;
        }

        if (ValidationUtils.isBlank(password)) {
            passwordInputLayout.setError(getString(R.string.validation_password_required));
            if (firstInvalidView == null) {
                firstInvalidView = passwordInput;
            }
            valid = false;
        }

        if (!valid && firstInvalidView != null) {
            firstInvalidView.requestFocus();
        }

        return valid;
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }

    private void updateRememberedEmail(String email) {
        SharedPreferences.Editor editor = getSharedPreferences(AUTH_PREFS_NAME, MODE_PRIVATE).edit();
        if (rememberMeCheckBox.isChecked()) {
            editor.putString(KEY_REMEMBERED_EMAIL, email);
        } else {
            editor.remove(KEY_REMEMBERED_EMAIL);
        }
        editor.apply();
    }

    private void routeAfterLogin(User user) {
        Intent intent;
        if (SessionManager.ROLE_ADMIN.equalsIgnoreCase(user.getRole())) {
            loginStatus.setText(R.string.login_admin_success_status);
            Toast.makeText(this, R.string.login_admin_success_toast, Toast.LENGTH_SHORT).show();
            intent = new Intent(this, AdminHomeActivity.class);
        } else {
            loginStatus.setText(R.string.login_user_success_status);
            Toast.makeText(this, R.string.login_user_success_toast, Toast.LENGTH_SHORT).show();
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoginLoading(boolean loading) {
        loginInProgress = loading;
        loginProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
        signUpButton.setEnabled(!loading);
    }

}
