package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import com.encs5150.students1220216_1220071.travelplanner.utils.ValidationUtils;

public class AdminAddAdminFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_add_admin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EditText emailInput = getActivity().findViewById(R.id.admin_email_input);
        EditText firstNameInput = getActivity().findViewById(R.id.admin_first_name_input);
        EditText lastNameInput = getActivity().findViewById(R.id.admin_last_name_input);
        EditText phoneInput = getActivity().findViewById(R.id.admin_phone_input);
        EditText passwordInput = getActivity().findViewById(R.id.admin_password_input);
        EditText confirmPasswordInput = getActivity().findViewById(R.id.admin_confirm_password_input);
        TextView statusView = getActivity().findViewById(R.id.admin_add_status);
        Button addButton = getActivity().findViewById(R.id.admin_add_button);

        UserRepository userRepository = new UserRepository(getActivity());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ValidationUtils.normalizeEmail(emailInput.getText().toString());
                String firstName = ValidationUtils.cleanInput(firstNameInput.getText().toString());
                String lastName = ValidationUtils.cleanInput(lastNameInput.getText().toString());
                String phone = ValidationUtils.normalizePhone(phoneInput.getText().toString());
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                // validate email
                if (!ValidationUtils.isValidEmail(email)) {
                    emailInput.setError("Enter a valid email.");
                    return;
                }

                // validate first name
                if (!ValidationUtils.isValidName(firstName)) {
                    firstNameInput.setError("First name must be at least 3 characters.");
                    return;
                }

                // validate last name
                if (!ValidationUtils.isValidName(lastName)) {
                    lastNameInput.setError("Last name must be at least 3 characters.");
                    return;
                }

                // validate phone
                if (!ValidationUtils.isValidPhone(phone)) {
                    phoneInput.setError("Enter a valid phone number.");
                    return;
                }

                // validate password
                if (!ValidationUtils.isValidPassword(password)) {
                    passwordInput.setError("Password must be at least 6 characters with a letter and number.");
                    return;
                }

                // validate confirm password
                if (!ValidationUtils.passwordsMatch(password, confirmPassword)) {
                    confirmPasswordInput.setError("Passwords do not match.");
                    return;
                }

                // check duplicate email
                if (userRepository.emailExists(email)) {
                    emailInput.setError("This email is already registered.");
                    return;
                }

                // create admin user
                User admin = new User();
                admin.setEmail(email);
                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setPhone(phone);
                admin.setPassword(password);
                admin.setGender("");
                admin.setCategory("");
                admin.setProfilePicturePath("");
                admin.setRole(SessionManager.ROLE_ADMIN);
                admin.setIsActive(1);

                boolean success = userRepository.registerAdmin(admin);

                if (success) {
                    statusView.setText("Admin added successfully.");
                    statusView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Admin added successfully.", Toast.LENGTH_SHORT).show();
                    // clear fields
                    emailInput.setText("");
                    firstNameInput.setText("");
                    lastNameInput.setText("");
                    phoneInput.setText("");
                    passwordInput.setText("");
                    confirmPasswordInput.setText("");
                } else {
                    statusView.setText("Failed to add admin. Please try again.");
                    statusView.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Failed to add admin.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
