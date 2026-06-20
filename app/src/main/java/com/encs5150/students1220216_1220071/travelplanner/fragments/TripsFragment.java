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

import com.encs5150.students1220216_1220071.travelplanner.MainActivity;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.adapters.TripAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;

import java.util.List;

public class TripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private TripRepository tripRepository;
    private TripAdapter tripAdapter;
    private TextView emptyState;
    private EditText searchInput;

    // tracks current search query and active filter
    private String currentQuery = "";
    private String currentFilterType = null;
    private double currentFilterValue = 0;
    private int currentFilterMin = 0;
    private int currentFilterMax = 0;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trips, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());
        emptyState = getActivity().findViewById(R.id.trips_empty_state);
        searchInput = getActivity().findViewById(R.id.trips_search_input);

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripAdapter = new TripAdapter(getActivity(), this);
        recyclerView.setAdapter(tripAdapter);

        // load all trips initially
        applyFilters();

        // search button
        Button searchButton = getActivity().findViewById(R.id.trips_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuery = searchInput.getText().toString();
                applyFilters();
            }
        });

        // filter all, clears everything and shows full list
        Button filterAll = getActivity().findViewById(R.id.filter_all);
        filterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuery = "";
                currentFilterType = null;
                currentFilterValue = 0;
                currentFilterMin = 0;
                currentFilterMax = 0;
                searchInput.setText("");
                applyFilters();
            }
        });

        // duration filters
        Button filterShortDuration = getActivity().findViewById(R.id.filter_duration_short);
        filterShortDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "duration";
                currentFilterMin = 1;
                currentFilterMax = 3;
                applyFilters();
            }
        });

        Button filterMediumDuration = getActivity().findViewById(R.id.filter_duration_medium);
        filterMediumDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "duration";
                currentFilterMin = 4;
                currentFilterMax = 7;
                applyFilters();
            }
        });

        Button filterLongDuration = getActivity().findViewById(R.id.filter_duration_long);
        filterLongDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "duration";
                currentFilterMin = 8;
                currentFilterMax = 100;
                applyFilters();
            }
        });

        // price filters
        Button filterPriceLow = getActivity().findViewById(R.id.filter_price_low);
        filterPriceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "price";
                currentFilterValue = 500;
                applyFilters();
            }
        });

        Button filterPriceMedium = getActivity().findViewById(R.id.filter_price_medium);
        filterPriceMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "price";
                currentFilterValue = 1000;
                applyFilters();
            }
        });

        Button filterPriceHigh = getActivity().findViewById(R.id.filter_price_high);
        filterPriceHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "price";
                currentFilterValue = 2000;
                applyFilters();
            }
        });

        // rating filter
        Button filterRatingHigh = getActivity().findViewById(R.id.filter_rating_high);
        filterRatingHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "rating";
                currentFilterValue = 4.5;
                applyFilters();
            }
        });

        Button filterRatingMedium = getActivity().findViewById(R.id.filter_rating_medium);
        filterRatingMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "rating";
                currentFilterValue = 4.0;
                applyFilters();
            }
        });

        Button filterRatingLow = getActivity().findViewById(R.id.filter_rating_low);
        filterRatingLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFilterType = "rating";
                currentFilterValue = 3.5;
                applyFilters();
            }
        });
    }

    // applies current search query and filter together
    private void applyFilters() {
        try {
            List<Trip> trips = tripRepository.searchWithFilter(
                    currentQuery,
                    currentFilterType,
                    currentFilterValue,
                    currentFilterMin,
                    currentFilterMax
            );
            emptyState.setText(R.string.trips_empty_state);
            loadTrips(trips);
        } catch (RuntimeException exception) {
            tripAdapter.setTrips(java.util.Collections.emptyList());
            emptyState.setText(R.string.data_load_error);
            emptyState.setVisibility(View.VISIBLE);
        }
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
        ((MainActivity) requireActivity()).openTripDetails(
                trip.getID(),
                TripDetailsFragment.SOURCE_TRIPS
        );
    }
}
