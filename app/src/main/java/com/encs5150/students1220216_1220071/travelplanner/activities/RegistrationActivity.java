package com.encs5150.students1220216_1220071.travelplanner.activities;

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
import com.google.android.material.button.MaterialButton;

public class RegistrationActivity extends AppCompatActivity {

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

        setupSpinner(R.id.gender_spinner, R.array.gender_options);
        setupSpinner(R.id.trip_category_spinner, R.array.trip_category_options);

        TextView registrationStatus = findViewById(R.id.registration_status);
        MaterialButton registerButton = findViewById(R.id.register_button);
        MaterialButton backToLoginButton = findViewById(R.id.back_to_login_button);

        registerButton.setOnClickListener(v -> {
            registrationStatus.setText(R.string.registration_pending_status);
            Toast.makeText(this, R.string.registration_pending_toast, Toast.LENGTH_LONG).show();
        });
        backToLoginButton.setOnClickListener(v -> finish());
    }

    private void setupSpinner(int spinnerId, int optionsArrayId) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                optionsArrayId,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
