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
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.adapters.FavoriteAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.FavoriteRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoriteAdapter.OnFavoriteActionListener {

    private FavoriteRepository favoriteRepository;
    private FavoriteAdapter favoriteAdapter;
    private TextView emptyState;
    private int currentUserId;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoriteRepository = new FavoriteRepository(getActivity());
        currentUserId = SessionManager.getCurrentUserId(getActivity());
        emptyState = getActivity().findViewById(R.id.favorites_empty_state);

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.favorites_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteAdapter = new FavoriteAdapter(getActivity(), this);
        recyclerView.setAdapter(favoriteAdapter);

        // load favorites
        loadFavorites();
    }

    // loads favorites for current user
    private void loadFavorites() {
        List<Trip> trips = favoriteRepository.getFavoriteTripsByUser(currentUserId);
        favoriteAdapter.setTrips(trips);
        if (trips.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // opens trip details
    @Override
    public void onTripClick(Trip trip) {
        TripDetailsFragment detailsFragment = TripDetailsFragment.newInstance(trip.getID());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }

    // removes trip from favorites and refreshes list
    @Override
    public void onRemoveClick(Trip trip) {
        favoriteRepository.removeFavorite(currentUserId, trip.getID());
        loadFavorites();
    }

    // opens reservation form directly from favorites
    @Override
    public void onReserveClick(Trip trip) {
        ReservationFormFragment reservationForm = ReservationFormFragment.newInstance(trip.getID(), trip.getDestination());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, reservationForm)
                .addToBackStack(null)
                .commit();
    }
}