package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

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

public class AdminTripsFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private TripRepository tripRepository;
    private TripAdapter tripAdapter;
    private TextView emptyState;

    private String currentQuery = "";  // track current search query

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_trips, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());
        emptyState = getActivity().findViewById(R.id.admin_trips_empty_state);

        // reuse TripAdapter with admin mode = true (hide favorite button)
        RecyclerView recyclerView = getActivity().findViewById(R.id.admin_trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripAdapter = new TripAdapter(getActivity(), this, true);
        recyclerView.setAdapter(tripAdapter);

        // load all trips initially
        loadTrips();

        // add trip button opens the form in add mode (tripId = -1 doesnt exist yet)
        Button addTripButton = getActivity().findViewById(R.id.admin_add_trip_button);
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminTripFormFragment addForm = AdminTripFormFragment.newInstance(-1);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_fragment_container, addForm)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // search button filters trips by destination/country/description
        EditText searchInput = getActivity().findViewById(R.id.admin_trips_search_input);
        Button searchButton = getActivity().findViewById(R.id.admin_trips_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuery = searchInput.getText().toString();
                if (currentQuery.isEmpty()) {
                    loadTrips();
                } else {
                    loadSearchResults(currentQuery);
                }
            }
        });
    }

    // loads all active trips from database
    private void loadTrips() {
        List<Trip> trips = tripRepository.getAllTrips();
        tripAdapter.setTrips(trips);
        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // loads trips matching the search query
    private void loadSearchResults(String query) {
        List<Trip> trips = tripRepository.searchTrips(query);
        tripAdapter.setTrips(trips);
        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // tapping a trip opens admin trip details instead of user trip details
    @Override
    public void onTripClick(Trip trip) {
        AdminTripDetailsFragment detailsFragment = AdminTripDetailsFragment.newInstance(trip.getID());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        // clear search when returning to trips list
        EditText searchInput = getActivity().findViewById(R.id.admin_trips_search_input);
        if (searchInput != null) {
            searchInput.setText("");
        }
        currentQuery = "";
        loadTrips();
    }
}