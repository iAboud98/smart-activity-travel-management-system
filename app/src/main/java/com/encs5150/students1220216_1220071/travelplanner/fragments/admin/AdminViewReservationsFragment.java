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
import com.encs5150.students1220216_1220071.travelplanner.adapters.AdminReservationAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;

import java.util.List;

public class AdminViewReservationsFragment extends Fragment {

    private ReservationRepository reservationRepository;
    private TextView emptyState;
    private AdminReservationAdapter adapter;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_view_reservations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reservationRepository = new ReservationRepository(getActivity());
        emptyState = getActivity().findViewById(R.id.admin_reservations_empty_state);
        adapter = new AdminReservationAdapter(getActivity());

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.admin_reservations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // load all reservations across all users
        List<Reservation> reservations = reservationRepository.getAllReservations();
        adapter.setReservations(reservations);

        if (reservations.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }

        // search button filters reservations by destination, country or user email
        EditText searchInput = getActivity().findViewById(R.id.admin_reservations_search_input);
        Button searchButton = getActivity().findViewById(R.id.admin_reservations_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString().trim();
                if (query.isEmpty()) {
                    loadReservations();
                } else {
                    List<Reservation> reservations = reservationRepository.searchReservations(query);
                    adapter.setReservations(reservations);
                    if (reservations.isEmpty()) {
                        emptyState.setVisibility(View.VISIBLE);
                    } else {
                        emptyState.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        adapter.setReservations(reservations);
        if (reservations.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }
}