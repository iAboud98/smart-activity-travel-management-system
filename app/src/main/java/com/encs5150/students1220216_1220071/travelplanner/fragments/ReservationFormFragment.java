package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationFormFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";
    private static final String ARG_TRIP_NAME = "trip_name";

    private int tripId;
    private String tripName;

    // creates a new instance with trip id and name as arguments
    public static ReservationFormFragment newInstance(int tripId, String tripName) {
        ReservationFormFragment fragment = new ReservationFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        args.putString(ARG_TRIP_NAME, tripName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservation_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get arguments
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
            tripName = getArguments().getString(ARG_TRIP_NAME);
        }

        // check user session
        int currentUserId = SessionManager.getCurrentUserId(getActivity());
        if (currentUserId == -1) {
            Toast.makeText(getActivity(), "Please login to make a reservation.", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, new TripsFragment())
                    .commit();
            return;
        }

        // check trip exists
        TripRepository tripRepository = new TripRepository(getActivity());
        Trip trip = tripRepository.getTripById(tripId);
        if (trip == null) {
            Toast.makeText(getActivity(), "Trip not found.", Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, new TripsFragment())
                    .commit();
            return;
        }

        // set trip name label
        TextView tripNameView = getActivity().findViewById(R.id.reservation_trip_name);
        tripNameView.setText(tripName);

        // setup reservation type spinner
        String[] reservationTypes = getActivity().getResources().getStringArray(R.array.reservation_type_options);
        Spinner typeSpinner = (Spinner) getActivity().findViewById(R.id.reservation_type_spinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, reservationTypes);
        typeSpinner.setAdapter(typeAdapter);

        // cancel button
        Button cancelButton = getActivity().findViewById(R.id.reservation_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // confirm button
        Button confirmButton = getActivity().findViewById(R.id.reservation_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText quantityInput = getActivity().findViewById(R.id.reservation_quantity);
                TextView typeError = getActivity().findViewById(R.id.reservation_type_error);
                EditText additionalInfoInput = getActivity().findViewById(R.id.reservation_additional_info);

                String quantityStr = quantityInput.getText().toString().trim();
                int selectedTypePosition = typeSpinner.getSelectedItemPosition();
                String additionalInfo = additionalInfoInput.getText().toString().trim();

                // validate quantity
                if (quantityStr.isEmpty()) {
                    quantityInput.setError("Please enter number of travelers.");
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    quantityInput.setError("Number of travelers must be at least 1.");
                    return;
                }

                // validate reservation type
                if (selectedTypePosition == 0) {
                    typeError.setVisibility(View.VISIBLE);
                    return;
                }
                typeError.setVisibility(View.GONE);

                // get current date
                String reservationDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                // create reservation object
                Reservation reservation = new Reservation();
                reservation.setUserId(currentUserId);
                reservation.setTripId(tripId);
                reservation.setQuantity(quantity);
                reservation.setReservationType(typeSpinner.getSelectedItem().toString());
                reservation.setReservationDate(reservationDate);
                reservation.setStatus("Confirmed");
                reservation.setAdditionalInfo(additionalInfo);

                // save to database
                ReservationRepository reservationRepository = new ReservationRepository(getActivity());
                boolean success = reservationRepository.createReservation(reservation);

                if (success) {
                    Toast.makeText(getActivity(), "Reservation confirmed", Toast.LENGTH_SHORT).show();
                    // navigate to my reservations
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, new MyReservationsFragment())
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Reservation failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}