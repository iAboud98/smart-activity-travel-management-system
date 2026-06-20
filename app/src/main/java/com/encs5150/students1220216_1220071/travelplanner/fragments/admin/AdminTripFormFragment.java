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
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;

public class AdminTripFormFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";

    // -1 means add new trip, any other value means edit existing trip
    private int tripId = -1;
    private TripRepository tripRepository;

    // creates instance — pass -1 for add mode, trip id for edit mode
    public static AdminTripFormFragment newInstance(int tripId) {
        AdminTripFormFragment fragment = new AdminTripFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_trip_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());

        // get trip id from arguments
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID, -1);
        }

        TextView formTitle = getActivity().findViewById(R.id.trip_form_title);
        EditText destinationInput = getActivity().findViewById(R.id.trip_form_destination);
        EditText countryInput = getActivity().findViewById(R.id.trip_form_country);
        EditText durationInput = getActivity().findViewById(R.id.trip_form_duration);
        EditText priceInput = getActivity().findViewById(R.id.trip_form_price);
        EditText ratingInput = getActivity().findViewById(R.id.trip_form_rating);
        EditText descriptionInput = getActivity().findViewById(R.id.trip_form_description);
        EditText imageUrlInput = getActivity().findViewById(R.id.trip_form_image_url);
        Button saveButton = getActivity().findViewById(R.id.trip_form_save_button);
        Button cancelButton = getActivity().findViewById(R.id.trip_form_cancel_button);

        if (tripId == -1) {
            // add mode — empty form
            formTitle.setText("Add New Trip");
        } else {
            // edit mode — pre-fill form with existing trip data
            formTitle.setText("Edit Trip");
            Trip trip = tripRepository.getTripById(tripId);
            if (trip != null) {
                destinationInput.setText(trip.getDestination());
                countryInput.setText(trip.getCountry());
                durationInput.setText(String.valueOf(trip.getDurationDays()));
                priceInput.setText(String.valueOf((int) trip.getPrice()));
                ratingInput.setText(String.valueOf(trip.getRating()));
                descriptionInput.setText(trip.getDescription());
                imageUrlInput.setText(trip.getImageUrl());
            }
        }

        // cancel goes back to trips list
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // save validates and saves the trip
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = destinationInput.getText().toString().trim();
                String country = countryInput.getText().toString().trim();
                String durationStr = durationInput.getText().toString().trim();
                String priceStr = priceInput.getText().toString().trim();
                String ratingStr = ratingInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();
                String imageUrl = imageUrlInput.getText().toString().trim();

                // validate all required fields
                if (destination.isEmpty()) {
                    destinationInput.setError("Destination is required.");
                    return;
                }
                if (country.isEmpty()) {
                    countryInput.setError("Country is required.");
                    return;
                }
                if (durationStr.isEmpty()) {
                    durationInput.setError("Duration is required.");
                    return;
                }
                if (priceStr.isEmpty()) {
                    priceInput.setError("Price is required.");
                    return;
                }
                if (ratingStr.isEmpty()) {
                    ratingInput.setError("Rating is required.");
                    return;
                }
                if (description.isEmpty()) {
                    descriptionInput.setError("Description is required.");
                    return;
                }
                if (imageUrl.isEmpty()) {
                    imageUrlInput.setError("Image URL is required.");
                    return;
                }

                // parse numeric values
                int duration = Integer.parseInt(durationStr);
                double price = Double.parseDouble(priceStr);
                double rating = Double.parseDouble(ratingStr);

                // validate numeric ranges
                if (duration <= 0) {
                    durationInput.setError("Duration must be at least 1 day.");
                    return;
                }
                if (price <= 0) {
                    priceInput.setError("Price must be greater than 0.");
                    return;
                }
                if (rating < 0 || rating > 5) {
                    ratingInput.setError("Rating must be between 0 and 5.");
                    return;
                }

                // build trip object from form values
                Trip trip = new Trip();
                trip.setDestination(destination);
                trip.setCountry(country);
                trip.setDurationDays(duration);
                trip.setPrice(price);
                trip.setRating(rating);
                trip.setDescription(description);
                trip.setImageUrl(imageUrl);
                trip.setIsActive(1);

                boolean success;
                if (tripId == -1) {
                    // add mode — insert new trip
                    // api_id set to 0 since this trip was created by admin not imported from API
                    trip.setApiID(0);
                    success = tripRepository.insertTrip(trip);
                } else {
                    // edit mode — update existing trip
                    trip.setID(tripId);
                    success = tripRepository.updateTrip(trip);
                }

                if (success) {
                    Toast.makeText(getActivity(),
                            tripId == -1 ? "Trip added successfully." : "Trip updated successfully.",
                            Toast.LENGTH_SHORT).show();
                    // go back to trips list
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getActivity(), "Failed to save trip. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}