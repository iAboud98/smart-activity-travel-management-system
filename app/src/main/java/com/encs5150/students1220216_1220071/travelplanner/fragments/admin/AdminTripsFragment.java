package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        // reuse TripAdapter, same as user trips list
        RecyclerView recyclerView = getActivity().findViewById(R.id.admin_trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripAdapter = new TripAdapter(getActivity(), this);
        recyclerView.setAdapter(tripAdapter);

        loadTrips();

        // add trip button opens form in add mode
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
    }

    private void loadTrips() {
        List<Trip> trips = tripRepository.getAllTrips();
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
}