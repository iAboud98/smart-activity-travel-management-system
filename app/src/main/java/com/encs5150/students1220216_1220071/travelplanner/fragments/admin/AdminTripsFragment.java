package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.adapters.AdminTripAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.TripRepository;
import java.util.List;

public class AdminTripsFragment extends Fragment implements AdminTripAdapter.OnAdminTripActionListener {

    private TripRepository tripRepository;
    private AdminTripAdapter adapter;
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

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.admin_trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminTripAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        // load all active trips
        loadTrips();
    }

    // loads all trips from database into the list
    private void loadTrips() {
        List<Trip> trips = tripRepository.getAllTrips();
        adapter.setTrips(trips);
        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // opens edit form with the selected trip's data
    @Override
    public void onEditClick(Trip trip) {
        AdminTripFormFragment editForm = AdminTripFormFragment.newInstance(trip.getID());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, editForm)
                .addToBackStack(null)
                .commit();
    }

    // placeholder for delete - will be implemented in B20
    @Override
    public void onDeleteClick(Trip trip) {
        Toast.makeText(getActivity(), "Delete coming in next update.", Toast.LENGTH_SHORT).show();
    }
}