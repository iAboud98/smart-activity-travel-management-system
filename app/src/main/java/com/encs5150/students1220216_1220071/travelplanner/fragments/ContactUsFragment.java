package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.R;

public class ContactUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.call_us_button).setOnClickListener(ignored -> openDialer());
        view.findViewById(R.id.locate_us_button).setOnClickListener(ignored -> openMap());
        view.findViewById(R.id.email_us_button).setOnClickListener(ignored -> openEmail());
    }

    private void openDialer() {
        Uri phoneUri = Uri.parse("tel:" + Uri.encode(getString(R.string.contact_phone_number)));
        openExternalActivity(
                new Intent(Intent.ACTION_DIAL, phoneUri),
                R.string.contact_no_dialer_toast
        );
    }

    private void openMap() {
        String location = getString(R.string.contact_location_query);
        Uri locationUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        openExternalActivity(
                new Intent(Intent.ACTION_VIEW, locationUri),
                R.string.contact_no_map_toast
        );
    }

    private void openEmail() {
        String address = getString(R.string.contact_email_address);
        String subject = getString(R.string.contact_email_subject);
        String body = getString(R.string.contact_email_body);
        Uri emailUri = Uri.parse(
                "mailto:" + Uri.encode(address)
                        + "?subject=" + Uri.encode(subject)
                        + "&body=" + Uri.encode(body)
        );
        openExternalActivity(
                new Intent(Intent.ACTION_SENDTO, emailUri),
                R.string.contact_no_email_app_toast
        );
    }

    private void openExternalActivity(Intent intent, @StringRes int unavailableMessageResId) {
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException exception) {
            Toast.makeText(requireContext(), unavailableMessageResId, Toast.LENGTH_SHORT).show();
        }
    }
}
