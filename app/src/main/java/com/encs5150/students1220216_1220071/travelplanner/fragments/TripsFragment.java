package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.adapters.TripAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;

import java.util.List;

public class TripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private TripRepository tripRepository;
    private TripAdapter tripAdapter;
    private TextView emptyState;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tripRepository = new TripRepository(requireContext());
        emptyState = view.findViewById(R.id.trips_empty_state);

        // setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tripAdapter = new TripAdapter(requireContext(), this);
        recyclerView.setAdapter(tripAdapter);

        // load all trips initially
        loadTrips(tripRepository.getAllTrips());

        // setup search
        EditText searchInput = view.findViewById(R.id.trips_search_input);
        Button searchButton = view.findViewById(R.id.trips_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString();
                if (query.isEmpty()) {
                    loadTrips(tripRepository.getAllTrips());
                } else {
                    loadTrips(tripRepository.searchTrips(query));
                }
            }
        });

        // setup filter buttons
        Button filterAll = view.findViewById(R.id.filter_all);
        Button filterShort = view.findViewById(R.id.filter_duration_short);
        Button filterMedium = view.findViewById(R.id.filter_duration_medium);
        Button filterLong = view.findViewById(R.id.filter_duration_long);
        Button filterPriceLow = view.findViewById(R.id.filter_price_low);
        Button filterPriceMedium = view.findViewById(R.id.filter_price_medium);
        Button filterPriceHigh = view.findViewById(R.id.filter_price_high);
        Button filterRating = view.findViewById(R.id.filter_rating);

        filterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.getAllTrips());
            }
        });

        filterShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByDuration(1, 3));
            }
        });

        filterMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByDuration(4, 7));
            }
        });

        filterLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByDuration(8, 100));
            }
        });

        filterPriceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByPrice(500));
            }
        });

        filterPriceMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByPrice(1000));
            }
        });

        filterPriceHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByPrice(2000));
            }
        });

        filterRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrips(tripRepository.filterByRating(4.5));
            }
        });
    }

    // loads trips into the adapter and handles empty state
    private void loadTrips(List<Trip> trips) {
        tripAdapter.setTrips(trips);
        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // opens trip details fragment when a trip is tapped
    @Override
    public void onTripClick(Trip trip) {
        TripDetailsFragment detailsFragment = TripDetailsFragment.newInstance(trip.getID());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
}