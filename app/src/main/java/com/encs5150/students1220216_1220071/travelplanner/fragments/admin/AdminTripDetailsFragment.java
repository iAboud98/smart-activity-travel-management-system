package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;

public class AdminTripDetailsFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";

    // tracks if delete button was already clicked once
    private boolean deleteConfirmPending = false;

    private int tripId;
    private TripRepository tripRepository;

    public static AdminTripDetailsFragment newInstance(int tripId) {
        AdminTripDetailsFragment fragment = new AdminTripDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_trip_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());

        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
        }

        Trip trip = tripRepository.getTripById(tripId);

        if (trip == null) {
            Toast.makeText(getActivity(), "Trip not found.", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        // fill views with trip data
        ImageView image = getActivity().findViewById(R.id.admin_details_image);
        TextView destination = getActivity().findViewById(R.id.admin_details_destination);
        TextView country = getActivity().findViewById(R.id.admin_details_country);
        TextView duration = getActivity().findViewById(R.id.admin_details_duration);
        TextView price = getActivity().findViewById(R.id.admin_details_price);
        TextView rating = getActivity().findViewById(R.id.admin_details_rating);
        TextView description = getActivity().findViewById(R.id.admin_details_description);
        Button editButton = getActivity().findViewById(R.id.admin_details_edit_button);
        Button deleteButton = getActivity().findViewById(R.id.admin_details_delete_button);

        destination.setText(trip.getDestination());
        country.setText(trip.getCountry());
        duration.setText(trip.getDurationDays() + " days");
        price.setText("$" + (int) trip.getPrice());
        rating.setText("★ " + trip.getRating());
        description.setText(trip.getDescription());

        Glide.with(getActivity())
                .load(trip.getImageUrl())
                .placeholder(R.drawable.ic_app_logo)
                .error(R.drawable.ic_app_logo)
                .into(image);

        // edit button opens form pre filled with this trip data
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminTripFormFragment editForm = AdminTripFormFragment.newInstance(tripId);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_fragment_container, editForm)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // delete button requires two clicks to confirm
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deleteConfirmPending) {
                    // first click, change button text to ask for confirmation
                    deleteConfirmPending = true;
                    deleteButton.setText("Confirm Delete");
                    Toast.makeText(getActivity(), "Tap again to confirm deletion.", Toast.LENGTH_SHORT).show();
                } else {
                    // second click, cancel all reservations for the trip then delete it
                    ReservationRepository reservationRepository = new ReservationRepository(getActivity());
                    reservationRepository.cancelReservationsByTrip(tripId);
                    boolean success = tripRepository.deleteTrip(tripId);
                    if (success) {
                        Toast.makeText(getActivity(), "Trip deleted and reservations cancelled.", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "Failed to delete trip.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}