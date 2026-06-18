package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;

public class ProfileManagementFragment extends Fragment {

    private ImageView profileImage;
    private TextView statusView;
    private View profileContent;
    private TextView fullNameView;
    private TextView summaryEmailView;
    private TextView emailValueView;
    private TextView firstNameValueView;
    private TextView lastNameValueView;
    private TextView genderValueView;
    private TextView preferenceValueView;
    private TextView phoneValueView;

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile_management, container, false);
        bindViews(view);
        loadProfile();
        return view;
    }

    private void bindViews(View view) {
        profileImage = view.findViewById(R.id.profile_image);
        statusView = view.findViewById(R.id.profile_status);
        profileContent = view.findViewById(R.id.profile_content);
        fullNameView = view.findViewById(R.id.profile_full_name);
        summaryEmailView = view.findViewById(R.id.profile_summary_email);
        emailValueView = view.findViewById(R.id.profile_email_value);
        firstNameValueView = view.findViewById(R.id.profile_first_name_value);
        lastNameValueView = view.findViewById(R.id.profile_last_name_value);
        genderValueView = view.findViewById(R.id.profile_gender_value);
        preferenceValueView = view.findViewById(R.id.profile_preference_value);
        phoneValueView = view.findViewById(R.id.profile_phone_value);
    }

    private void loadProfile() {
        try {
            User user = loadCurrentUser();
            if (user == null) {
                showError();
                return;
            }

            showProfile(user);
        } catch (RuntimeException exception) {
            showError();
        }
    }

    @Nullable
    private User loadCurrentUser() {
        String sessionEmail = SessionManager.getCurrentUserEmail(requireContext());
        if (ValidationUtils.isBlank(sessionEmail)) {
            return null;
        }

        User user = new UserRepository(requireContext()).findUserByEmail(sessionEmail);
        if (user == null) {
            return null;
        }

        int sessionUserId = SessionManager.getCurrentUserId(requireContext());
        if (sessionUserId >= 0 && user.getId() != sessionUserId) {
            return null;
        }

        return user;
    }

    private void showProfile(User user) {
        statusView.setVisibility(View.GONE);
        profileContent.setVisibility(View.VISIBLE);

        String fullName = buildFullName(user.getFirstName(), user.getLastName());
        String displayName = ValidationUtils.isBlank(fullName)
                ? getString(R.string.profile_unknown_traveler)
                : fullName;

        fullNameView.setText(displayName);
        summaryEmailView.setText(formatValue(user.getEmail()));
        emailValueView.setText(formatValue(user.getEmail()));
        firstNameValueView.setText(formatValue(user.getFirstName()));
        lastNameValueView.setText(formatValue(user.getLastName()));
        genderValueView.setText(formatValue(user.getGender()));
        preferenceValueView.setText(formatValue(user.getCategory()));
        phoneValueView.setText(formatValue(user.getPhone()));
        loadProfileImage(user.getProfilePicturePath());
    }

    private void showError() {
        profileContent.setVisibility(View.GONE);
        statusView.setText(R.string.profile_load_error);
        statusView.setVisibility(View.VISIBLE);
        profileImage.setImageResource(R.drawable.ic_nav_profile);
    }

    private void loadProfileImage(String profilePicturePath) {
        if (ValidationUtils.isBlank(profilePicturePath)) {
            profileImage.setImageResource(R.drawable.ic_nav_profile);
            profileImage.setContentDescription(getString(R.string.profile_default_image_content_description));
            return;
        }

        profileImage.setContentDescription(getString(R.string.profile_image_content_description));
        Glide.with(this)
                .load(profilePicturePath)
                .placeholder(R.drawable.ic_nav_profile)
                .error(R.drawable.ic_nav_profile)
                .circleCrop()
                .into(profileImage);
    }

    private String buildFullName(String firstName, String lastName) {
        return (ValidationUtils.cleanInput(firstName) + " " + ValidationUtils.cleanInput(lastName)).trim();
    }

    private String formatValue(String value) {
        String cleanValue = ValidationUtils.cleanInput(value);
        return ValidationUtils.isBlank(cleanValue)
                ? getString(R.string.profile_value_not_provided)
                : cleanValue;
    }
}
