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
import com.encs5150.students1220216_1220071.travelplanner.adapters.ReservationAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import java.util.List;

public class MyReservationsFragment extends Fragment implements ReservationAdapter.OnReservationClickListener {

    private ReservationRepository reservationRepository;
    private ReservationAdapter reservationAdapter;
    private TextView emptyState;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_reservations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reservationRepository = new ReservationRepository(getActivity());
        emptyState = getActivity().findViewById(R.id.reservations_empty_state);

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.reservations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reservationAdapter = new ReservationAdapter(getActivity(), this);
        recyclerView.setAdapter(reservationAdapter);

        // load reservations for current user
        int currentUserId = SessionManager.getCurrentUserId(getActivity());
        List<Reservation> reservations;
        try {
            reservations = reservationRepository.getReservationsByUser(currentUserId);
            emptyState.setText(R.string.reservations_empty_state);
        } catch (RuntimeException exception) {
            reservations = java.util.Collections.emptyList();
            emptyState.setText(R.string.data_load_error);
        }

        reservationAdapter.setReservations(reservations);
        if (reservations.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // opens trip details when a reservation row is tapped
    @Override
    public void onReservationClick(Reservation reservation) {
        ((MainActivity) requireActivity()).openTripDetails(
                reservation.getTripId(),
                TripDetailsFragment.SOURCE_RESERVATIONS
        );
    }
}
