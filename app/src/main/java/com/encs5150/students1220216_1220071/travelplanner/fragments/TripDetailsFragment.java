package com.encs5150.students1220216_1220071.travelplanner.fragments;

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
import com.encs5150.students1220216_1220071.travelplanner.repositories.FavoriteRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

public class TripDetailsFragment extends Fragment {

    private static final String ARG_TRIP_ID = "trip_id";

    private int tripId;
    private Trip trip;
    private TripRepository tripRepository;
    private FavoriteRepository favoriteRepository;
    private int currentUserId;

    // creates a new instance of this fragment with the trip id as argument
    public static TripDetailsFragment newInstance(int tripId) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());
        favoriteRepository = new FavoriteRepository(getActivity());
        currentUserId = SessionManager.getCurrentUserId(getActivity());

        // get trip id from arguments
        if (getArguments() != null) {
            tripId = getArguments().getInt(ARG_TRIP_ID);
        }

        // load trip from database by id
        trip = tripRepository.getTripById(tripId);

        if (trip == null) {
            // trip not found, show error and hide everything else
            getActivity().findViewById(R.id.details_image).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_destination).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_country).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_duration).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_price).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_rating).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_description).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_favorite_button).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_reserve_button).setVisibility(View.GONE);
            getActivity().findViewById(R.id.details_error).setVisibility(View.VISIBLE);

            Button backButton = getActivity().findViewById(R.id.details_back_button);
            backButton.setText("Back to Trips");
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, new TripsFragment())
                            .commit();
                }
            });
            return;
        }

        // fill views with trip data
        ImageView image = getActivity().findViewById(R.id.details_image);
        TextView destination = getActivity().findViewById(R.id.details_destination);
        TextView country = getActivity().findViewById(R.id.details_country);
        TextView duration = getActivity().findViewById(R.id.details_duration);
        TextView price = getActivity().findViewById(R.id.details_price);
        TextView rating = getActivity().findViewById(R.id.details_rating);
        TextView description = getActivity().findViewById(R.id.details_description);
        Button favoriteButton = getActivity().findViewById(R.id.details_favorite_button);
        Button reserveButton = getActivity().findViewById(R.id.details_reserve_button);

        destination.setText(trip.getDestination());
        country.setText(trip.getCountry());
        duration.setText(trip.getDurationDays() + " days");
        price.setText("$" + (int) trip.getPrice());
        rating.setText("★ " + trip.getRating());
        description.setText(trip.getDescription());

        // load image using Glide
        Glide.with(getActivity())
                .load(trip.getImageUrl())
                .placeholder(R.drawable.ic_app_logo)
                .error(R.drawable.ic_app_logo)
                .into(image);

        // set favorite button text based on current state
        updateFavoriteButton(favoriteButton);

        // favorite button click
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteRepository.isFavorite(currentUserId, tripId)) {
                    favoriteRepository.removeFavorite(currentUserId, tripId);
                    Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    favoriteRepository.addFavorite(currentUserId, tripId);
                    Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                }
                updateFavoriteButton(favoriteButton);
            }
        });

        // reserve button click, opens reservation form fragment
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationFormFragment reservationForm = ReservationFormFragment.newInstance(tripId, trip.getDestination());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, reservationForm)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    // updates the favorite button text based on whether the trip is currently favorited
    private void updateFavoriteButton(Button favoriteButton) {
        if (favoriteRepository.isFavorite(currentUserId, tripId)) {
            favoriteButton.setText("Remove from Favorites");
        } else {
            favoriteButton.setText("Add to Favorites");
        }
    }
}