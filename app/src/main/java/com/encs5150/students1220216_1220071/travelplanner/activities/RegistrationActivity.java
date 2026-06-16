package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistrationActivity extends AppCompatActivity {
    private TextInputLayout emailInputLayout;
    private TextInputLayout firstNameInputLayout;
    private TextInputLayout lastNameInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private TextInputLayout phoneInputLayout;
    private TextInputEditText emailInput;
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputEditText phoneInput;
    private Spinner genderSpinner;
    private Spinner tripCategorySpinner;
    private TextView genderError;
    private TextView tripCategoryError;
    private TextView registrationStatus;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        View registrationRoot = findViewById(R.id.registration_root);
        ViewCompat.setOnApplyWindowInsetsListener(registrationRoot, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInputLayout = findViewById(R.id.registration_email_input_layout);
        firstNameInputLayout = findViewById(R.id.first_name_input_layout);
        lastNameInputLayout = findViewById(R.id.last_name_input_layout);
        passwordInputLayout = findViewById(R.id.registration_password_input_layout);
        confirmPasswordInputLayout = findViewById(R.id.confirm_password_input_layout);
        phoneInputLayout = findViewById(R.id.phone_input_layout);
        emailInput = findViewById(R.id.registration_email_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        passwordInput = findViewById(R.id.registration_password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        phoneInput = findViewById(R.id.phone_input);
        genderSpinner = findViewById(R.id.gender_spinner);
        tripCategorySpinner = findViewById(R.id.trip_category_spinner);
        genderError = findViewById(R.id.gender_error);
        tripCategoryError = findViewById(R.id.trip_category_error);
        registrationStatus = findViewById(R.id.registration_status);
        userRepository = new UserRepository(this);

        setupSpinner(genderSpinner, R.array.gender_options);
        setupSpinner(tripCategorySpinner, R.array.trip_category_options);

        MaterialButton registerButton = findViewById(R.id.register_button);
        MaterialButton backToLoginButton = findViewById(R.id.back_to_login_button);

        registerButton.setOnClickListener(v -> {
            if (validateRegistrationForm()) {
                registerUser();
            } else {
                registrationStatus.setText(R.string.registration_validation_error_status);
            }
        });
        backToLoginButton.setOnClickListener(v -> finish());
    }

    private void setupSpinner(Spinner spinner, int optionsArrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                optionsArrayId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void registerUser() {
        String email = ValidationUtils.normalizeEmail(getText(emailInput));
        if (userRepository.emailExists(email)) {
            emailInputLayout.setError(getString(R.string.registration_duplicate_email_error));
            emailInput.requestFocus();
            registrationStatus.setText(R.string.registration_validation_error_status);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(ValidationUtils.cleanInput(getText(firstNameInput)));
        user.setLastName(ValidationUtils.cleanInput(getText(lastNameInput)));
        user.setPassword(getText(passwordInput));
        user.setGender(genderSpinner.getSelectedItem().toString());
        user.setCategory(tripCategorySpinner.getSelectedItem().toString());
        user.setPhone(ValidationUtils.normalizePhone(getText(phoneInput)));
        user.setProfilePicturePath("");
        user.setRole(SessionManager.ROLE_USER);
        user.setIsActive(1);

        if (!userRepository.registerUser(user)) {
            registrationStatus.setText(R.string.registration_failure_status);
            Toast.makeText(this, R.string.registration_failure_toast, Toast.LENGTH_LONG).show();
            return;
        }

        registrationStatus.setText(R.string.registration_success_status);
        Toast.makeText(this, R.string.registration_success_toast, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_PREFILL_EMAIL, email);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean validateRegistrationForm() {
        clearErrors();

        String email = getText(emailInput);
        String firstName = getText(firstNameInput);
        String lastName = getText(lastNameInput);
        String password = getText(passwordInput);
        String confirmPassword = getText(confirmPasswordInput);
        String phone = getText(phoneInput);

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

        if (!ValidationUtils.isValidName(firstName)) {
            firstNameInputLayout.setError(getString(R.string.validation_first_name_length));
            if (firstInvalidView == null) {
                firstInvalidView = firstNameInput;
            }
            valid = false;
        }

        if (!ValidationUtils.isValidName(lastName)) {
            lastNameInputLayout.setError(getString(R.string.validation_last_name_length));
            if (firstInvalidView == null) {
                firstInvalidView = lastNameInput;
            }
            valid = false;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            passwordInputLayout.setError(getString(R.string.validation_password_rules));
            if (firstInvalidView == null) {
                firstInvalidView = passwordInput;
            }
            valid = false;
        }

        if (ValidationUtils.isBlank(confirmPassword)) {
            confirmPasswordInputLayout.setError(getString(R.string.validation_confirm_password_required));
            if (firstInvalidView == null) {
                firstInvalidView = confirmPasswordInput;
            }
            valid = false;
        } else if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
            confirmPasswordInputLayout.setError(getString(R.string.validation_password_match));
            if (firstInvalidView == null) {
                firstInvalidView = confirmPasswordInput;
            }
            valid = false;
        }

        if (!ValidationUtils.isValidSpinnerSelection(genderSpinner.getSelectedItemPosition())) {
            genderError.setText(R.string.validation_gender_required);
            genderError.setVisibility(View.VISIBLE);
            if (firstInvalidView == null) {
                firstInvalidView = genderSpinner;
            }
            valid = false;
        }

        if (!ValidationUtils.isValidSpinnerSelection(tripCategorySpinner.getSelectedItemPosition())) {
            tripCategoryError.setText(R.string.validation_trip_category_required);
            tripCategoryError.setVisibility(View.VISIBLE);
            if (firstInvalidView == null) {
                firstInvalidView = tripCategorySpinner;
            }
            valid = false;
        }

        if (ValidationUtils.isBlank(phone)) {
            phoneInputLayout.setError(getString(R.string.validation_phone_required));
            if (firstInvalidView == null) {
                firstInvalidView = phoneInput;
            }
            valid = false;
        } else if (!ValidationUtils.isValidPhone(phone)) {
            phoneInputLayout.setError(getString(R.string.validation_phone_format));
            if (firstInvalidView == null) {
                firstInvalidView = phoneInput;
            }
            valid = false;
        }

        if (!valid && firstInvalidView != null) {
            firstInvalidView.requestFocus();
        }

        return valid;
    }

    private void clearErrors() {
        emailInputLayout.setError(null);
        firstNameInputLayout.setError(null);
        lastNameInputLayout.setError(null);
        passwordInputLayout.setError(null);
        confirmPasswordInputLayout.setError(null);
        phoneInputLayout.setError(null);
        genderError.setVisibility(View.GONE);
        tripCategoryError.setVisibility(View.GONE);
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
