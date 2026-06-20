package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SpecialSectionFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private TripRepository tripRepository;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_special_section, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tripRepository = new TripRepository(getActivity());

        try {
            setupSection(
                    R.id.top_rated_recycler_view,
                    R.id.top_rated_empty,
                    tripRepository.getTopRatedTrips()
            );
            setupSection(
                    R.id.popular_recycler_view,
                    R.id.popular_empty,
                    tripRepository.getPopularTrips()
            );
            setupSection(
                    R.id.trending_recycler_view,
                    R.id.trending_empty,
                    tripRepository.getTrendingTrips()
            );
        } catch (RuntimeException exception) {
            showSectionError(R.id.top_rated_recycler_view, R.id.top_rated_empty);
            showSectionError(R.id.popular_recycler_view, R.id.popular_empty);
            showSectionError(R.id.trending_recycler_view, R.id.trending_empty);
        }
    }

    // sets up a RecyclerView section with trips and handles empty state
    private void setupSection(int recyclerViewId, int emptyStateId, List<Trip> trips) {
        TextView emptyState = getActivity().findViewById(emptyStateId);
        RecyclerView recyclerView = getActivity().findViewById(recyclerViewId);

        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            TripAdapter adapter = new TripAdapter(getActivity(), this);
            adapter.setTrips(trips);
            recyclerView.setAdapter(adapter);
        }
    }

    private void showSectionError(int recyclerViewId, int emptyStateId) {
        TextView emptyState = requireActivity().findViewById(emptyStateId);
        RecyclerView recyclerView = requireActivity().findViewById(recyclerViewId);
        emptyState.setText(R.string.data_load_error);
        emptyState.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    // opens trip details when a trip is tapped
    @Override
    public void onTripClick(Trip trip) {
        ((MainActivity) requireActivity()).openTripDetails(
                trip.getID(),
                TripDetailsFragment.SOURCE_SPECIAL
        );
    }
}
