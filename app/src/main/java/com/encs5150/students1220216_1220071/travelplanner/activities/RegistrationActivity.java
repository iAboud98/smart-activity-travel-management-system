package com.encs5150.students1220216_1220071.travelplanner.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.encs5150.students1220216_1220071.travelplanner.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_placeholder);

        TextView title = findViewById(R.id.placeholder_title);
        TextView description = findViewById(R.id.placeholder_description);

        title.setText(R.string.registration_placeholder_title);
        description.setText(R.string.registration_placeholder_description);
    }
}
