package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileManagementFragment extends Fragment {

    private ActivityResultLauncher<String[]> profilePicturePicker;
    private User currentUser;
    private String originalProfilePicturePath = "";
    private String selectedProfilePicturePath = "";

    private ImageView profileImage;
    private TextView statusView;
    private View profileContent;
    private TextView fullNameView;
    private TextView summaryEmailView;
    private TextView emailValueView;
    private TextView genderValueView;
    private TextView preferenceValueView;
    private TextInputLayout firstNameInputLayout;
    private TextInputLayout lastNameInputLayout;
    private TextInputLayout phoneInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private TextInputEditText phoneInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profilePicturePicker = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                this::handleProfilePictureSelected
        );
    }

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile_management, container, false);
        bindViews(view);
        setupActions(view);
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
        genderValueView = view.findViewById(R.id.profile_gender_value);
        preferenceValueView = view.findViewById(R.id.profile_preference_value);
        firstNameInputLayout = view.findViewById(R.id.profile_first_name_input_layout);
        lastNameInputLayout = view.findViewById(R.id.profile_last_name_input_layout);
        phoneInputLayout = view.findViewById(R.id.profile_phone_input_layout);
        passwordInputLayout = view.findViewById(R.id.profile_password_input_layout);
        confirmPasswordInputLayout = view.findViewById(R.id.profile_confirm_password_input_layout);
        firstNameInput = view.findViewById(R.id.profile_first_name_input);
        lastNameInput = view.findViewById(R.id.profile_last_name_input);
        phoneInput = view.findViewById(R.id.profile_phone_input);
        passwordInput = view.findViewById(R.id.profile_password_input);
        confirmPasswordInput = view.findViewById(R.id.profile_confirm_password_input);
    }

    private void setupActions(View view) {
        view.findViewById(R.id.select_profile_picture_button).setOnClickListener(v -> openProfilePicturePicker());
        view.findViewById(R.id.save_profile_button).setOnClickListener(v -> saveProfile());
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
        currentUser = user;
        originalProfilePicturePath = ValidationUtils.cleanInput(user.getProfilePicturePath());
        selectedProfilePicturePath = originalProfilePicturePath;
        statusView.setVisibility(View.GONE);
        profileContent.setVisibility(View.VISIBLE);

        String fullName = buildFullName(user.getFirstName(), user.getLastName());
        String displayName = ValidationUtils.isBlank(fullName)
                ? getString(R.string.profile_unknown_traveler)
                : fullName;

        fullNameView.setText(displayName);
        summaryEmailView.setText(formatValue(user.getEmail()));
        emailValueView.setText(formatValue(user.getEmail()));
        genderValueView.setText(formatValue(user.getGender()));
        preferenceValueView.setText(formatValue(user.getCategory()));
        firstNameInput.setText(ValidationUtils.cleanInput(user.getFirstName()));
        lastNameInput.setText(ValidationUtils.cleanInput(user.getLastName()));
        phoneInput.setText(ValidationUtils.cleanInput(user.getPhone()));
        clearPasswordInputs();
        clearValidationErrors();
        loadProfileImage(user.getProfilePicturePath());
    }

    private void showError() {
        currentUser = null;
        profileContent.setVisibility(View.GONE);
        showStatus(R.string.profile_load_error, R.color.color_error);
        profileImage.setImageResource(R.drawable.ic_nav_profile);
    }

    private void openProfilePicturePicker() {
        profilePicturePicker.launch(new String[]{"image/*"});
    }

    private void handleProfilePictureSelected(@Nullable Uri uri) {
        if (uri == null) {
            return;
        }

        try {
            requireContext().getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        } catch (SecurityException ignored) {
            // Some providers grant temporary read access only; Glide can still preview it in this session.
        }

        selectedProfilePicturePath = uri.toString();
        loadProfileImage(selectedProfilePicturePath);
        showStatus(R.string.profile_picture_selected_status, R.color.color_primary);
    }

    private void saveProfile() {
        if (currentUser == null) {
            showError();
            return;
        }

        clearValidationErrors();
        String firstName = ValidationUtils.cleanInput(readInput(firstNameInput));
        String lastName = ValidationUtils.cleanInput(readInput(lastNameInput));
        String phone = ValidationUtils.normalizePhone(readInput(phoneInput));
        String password = readInput(passwordInput);
        String confirmPassword = readInput(confirmPasswordInput);

        if (!validateProfileInputs(firstName, lastName, phone, password, confirmPassword)) {
            showStatus(R.string.profile_validation_error_status, R.color.color_error);
            return;
        }

        try {
            UserRepository userRepository = new UserRepository(requireContext());
            boolean updated = userRepository.updateFirstName(currentUser.getId(), firstName)
                    && userRepository.updateLastName(currentUser.getId(), lastName)
                    && userRepository.updatePhone(currentUser.getId(), phone);

            if (updated && !ValidationUtils.isBlank(password)) {
                updated = userRepository.updatePassword(currentUser.getId(), password);
            }

            if (updated && hasSelectedProfilePictureChanged()) {
                updated = userRepository.updateProfilePicture(currentUser.getId(), selectedProfilePicturePath);
            }

            if (!updated) {
                showSaveFailure();
                return;
            }

            User refreshedUser = userRepository.findUserByEmail(currentUser.getEmail());
            if (refreshedUser == null) {
                showError();
                return;
            }

            showProfile(refreshedUser);
            showStatus(R.string.profile_update_success_status, R.color.color_primary);
            Toast.makeText(requireContext(), R.string.profile_update_success_toast, Toast.LENGTH_SHORT).show();
        } catch (RuntimeException exception) {
            showSaveFailure();
        }
    }

    private boolean validateProfileInputs(
            String firstName,
            String lastName,
            String phone,
            String password,
            String confirmPassword
    ) {
        boolean isValid = true;

        if (!ValidationUtils.isValidName(firstName)) {
            firstNameInputLayout.setError(getString(R.string.validation_first_name_length));
            isValid = false;
        }

        if (!ValidationUtils.isValidName(lastName)) {
            lastNameInputLayout.setError(getString(R.string.validation_last_name_length));
            isValid = false;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            phoneInputLayout.setError(getString(R.string.validation_phone_format));
            isValid = false;
        }

        boolean wantsPasswordUpdate = !ValidationUtils.isBlank(password)
                || !ValidationUtils.isBlank(confirmPassword);
        if (wantsPasswordUpdate) {
            if (ValidationUtils.isBlank(password)) {
                passwordInputLayout.setError(getString(R.string.validation_password_required));
                isValid = false;
            } else if (!ValidationUtils.isValidPassword(password)) {
                passwordInputLayout.setError(getString(R.string.validation_password_rules));
                isValid = false;
            }

            if (ValidationUtils.isBlank(confirmPassword)) {
                confirmPasswordInputLayout.setError(getString(R.string.validation_confirm_password_required));
                isValid = false;
            } else if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
                confirmPasswordInputLayout.setError(getString(R.string.validation_password_match));
                isValid = false;
            }
        }

        return isValid;
    }

    private void showSaveFailure() {
        showStatus(R.string.profile_update_failure_status, R.color.color_error);
        Toast.makeText(requireContext(), R.string.profile_update_failure_toast, Toast.LENGTH_SHORT).show();
    }

    private void clearValidationErrors() {
        firstNameInputLayout.setError(null);
        lastNameInputLayout.setError(null);
        phoneInputLayout.setError(null);
        passwordInputLayout.setError(null);
        confirmPasswordInputLayout.setError(null);
    }

    private void clearPasswordInputs() {
        passwordInput.setText("");
        confirmPasswordInput.setText("");
    }

    private boolean hasSelectedProfilePictureChanged() {
        return !originalProfilePicturePath.equals(ValidationUtils.cleanInput(selectedProfilePicturePath));
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

    private void showStatus(int messageResId, int colorResId) {
        statusView.setText(messageResId);
        statusView.setTextColor(ContextCompat.getColor(requireContext(), colorResId));
        statusView.setVisibility(View.VISIBLE);
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

    private String readInput(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString();
    }
}
