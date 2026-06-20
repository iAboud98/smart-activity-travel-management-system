package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.graphics.ColorFilter;
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

    private String currentQuery = ""; // tracks current search query

    // tracks one active filter per category, 0 means no filter
    private int currentDurationMin = 0;
    private int currentDurationMax = 0;
    private double currentMaxPrice = 0;
    private double currentMinRating = 0;

    // keeping references to filter buttons to toggle their appearance
    private Button activeDurationButton = null;
    private Button activePriceButton = null;
    private Button activeRatingButton = null;

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

        // all button clears all filters and keeps search query
        Button filterAll = getActivity().findViewById(R.id.filter_all);
        filterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateButton(activeDurationButton);
                deactivateButton(activePriceButton);
                deactivateButton(activeRatingButton);
                activeDurationButton = null;
                activePriceButton = null;
                activeRatingButton = null;
                currentDurationMin = 0;
                currentDurationMax = 0;
                currentMaxPrice = 0;
                currentMinRating = 0;
                applyFilters();
            }
        });

        // duration filters
        Button filterShortDuration = getActivity().findViewById(R.id.filter_duration_short);
        filterShortDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDurationButton == filterShortDuration) {
                    // clicking active button again deactivates it
                    deactivateButton(filterShortDuration);
                    activeDurationButton = null;
                    currentDurationMin = 0;
                    currentDurationMax = 0;
                } else {
                    deactivateButton(activeDurationButton);
                    activateButton(filterShortDuration);
                    activeDurationButton = filterShortDuration;
                    currentDurationMin = 1;
                    currentDurationMax = 3;
                }
                applyFilters();
            }
        });

        Button filterMediumDuration = getActivity().findViewById(R.id.filter_duration_medium);
        filterMediumDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDurationButton == filterMediumDuration) {
                    deactivateButton(filterMediumDuration);
                    activeDurationButton = null;
                    currentDurationMin = 0;
                    currentDurationMax = 0;
                } else {
                    deactivateButton(activeDurationButton);
                    activateButton(filterMediumDuration);
                    activeDurationButton = filterMediumDuration;
                    currentDurationMin = 4;
                    currentDurationMax = 7;
                }
                applyFilters();
            }
        });

        Button filterLongDuration = getActivity().findViewById(R.id.filter_duration_long);
        filterLongDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeDurationButton == filterLongDuration) {
                    deactivateButton(filterLongDuration);
                    activeDurationButton = null;
                    currentDurationMin = 0;
                    currentDurationMax = 0;
                } else {
                    deactivateButton(activeDurationButton);
                    activateButton(filterLongDuration);
                    activeDurationButton = filterLongDuration;
                    currentDurationMin = 8;
                    currentDurationMax = 100;
                }
                applyFilters();
            }
        });

        // price filters
        Button filterPriceLow = getActivity().findViewById(R.id.filter_price_low);
        filterPriceLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activePriceButton == filterPriceLow) {
                    deactivateButton(filterPriceLow);
                    activePriceButton = null;
                    currentMaxPrice = 0;
                } else {
                    deactivateButton(activePriceButton);
                    activateButton(filterPriceLow);
                    activePriceButton = filterPriceLow;
                    currentMaxPrice = 500;
                }
                applyFilters();
            }
        });

        Button filterPriceMedium = getActivity().findViewById(R.id.filter_price_medium);
        filterPriceMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activePriceButton == filterPriceMedium) {
                    deactivateButton(filterPriceMedium);
                    activePriceButton = null;
                    currentMaxPrice = 0;
                } else {
                    deactivateButton(activePriceButton);
                    activateButton(filterPriceMedium);
                    activePriceButton = filterPriceMedium;
                    currentMaxPrice = 1000;
                }
                applyFilters();
            }
        });

        Button filterPriceHigh = getActivity().findViewById(R.id.filter_price_high);
        filterPriceHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activePriceButton == filterPriceHigh) {
                    deactivateButton(filterPriceHigh);
                    activePriceButton = null;
                    currentMaxPrice = 0;
                } else {
                    deactivateButton(activePriceButton);
                    activateButton(filterPriceHigh);
                    activePriceButton = filterPriceHigh;
                    currentMaxPrice = 2000;
                }
                applyFilters();
            }
        });

        // rating filters
        Button filterRatingHigh = getActivity().findViewById(R.id.filter_rating_high);
        filterRatingHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeRatingButton == filterRatingHigh) {
                    deactivateButton(filterRatingHigh);
                    activeRatingButton = null;
                    currentMinRating = 0;
                } else {
                    deactivateButton(activeRatingButton);
                    activateButton(filterRatingHigh);
                    activeRatingButton = filterRatingHigh;
                    currentMinRating = 4.5;
                }
                applyFilters();
            }
        });

        Button filterRatingMedium = getActivity().findViewById(R.id.filter_rating_medium);
        filterRatingMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeRatingButton == filterRatingMedium) {
                    deactivateButton(filterRatingMedium);
                    activeRatingButton = null;
                    currentMinRating = 0;
                } else {
                    deactivateButton(activeRatingButton);
                    activateButton(filterRatingMedium);
                    activeRatingButton = filterRatingMedium;
                    currentMinRating = 4.0;
                }
                applyFilters();
            }
        });

        Button filterRatingLow = getActivity().findViewById(R.id.filter_rating_low);
        filterRatingLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activeRatingButton == filterRatingLow) {
                    deactivateButton(filterRatingLow);
                    activeRatingButton = null;
                    currentMinRating = 0;
                } else {
                    deactivateButton(activeRatingButton);
                    activateButton(filterRatingLow);
                    activeRatingButton = filterRatingLow;
                    currentMinRating = 3.5;
                }
                applyFilters();
            }
        });
    }

    // visually marks a button as active by reducing opacity
    private void activateButton(Button button) {
        if (button != null) {
            button.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getActivity().getResources().getColor(R.color.color_secondary)
                    )
            );
        }
    }

    // restores button to its default appearance
    private void deactivateButton(Button button) {
        if (button != null) {
            button.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getActivity().getResources().getColor(R.color.color_primary)
                    )
            );
        }
    }

    // applies all active filters and search query together
    private void applyFilters() {
        try {
            List<Trip> trips = tripRepository.searchWithFilter(
                    currentQuery,
                    currentDurationMin,
                    currentDurationMax,
                    currentMaxPrice,
                    currentMinRating
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

    // opens trip details when a trip is tapped
    @Override
    public void onTripClick(Trip trip) {
        ((MainActivity) requireActivity()).openTripDetails(
                trip.getID(),
                TripDetailsFragment.SOURCE_TRIPS
        );
    }
}