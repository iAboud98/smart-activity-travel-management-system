package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.encs5150.students1220216_1220071.travelplanner.adapters.AdminUserAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import java.util.List;

public class AdminViewUsersFragment extends Fragment implements AdminUserAdapter.OnUserDeleteListener {

    private UserRepository userRepository;
    private AdminUserAdapter adapter;
    private TextView emptyState;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_view_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userRepository = new UserRepository(getActivity());
        emptyState = getActivity().findViewById(R.id.users_empty_state);

        // setup RecyclerView with delete listener enabled
        RecyclerView recyclerView = getActivity().findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminUserAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    // loads all active regular users
    private void loadUsers() {
        List<User> users = userRepository.getAllUsers();
        adapter.setUsers(users);
        if (users.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    // called when admin taps delete on a user row
    @Override
    public void onDeleteClick(User user) {
        // prevent admin from deleting themselves
        int currentAdminId = SessionManager.getCurrentUserId(getActivity());
        if (user.getId() == currentAdminId) {
            Toast.makeText(getActivity(), "You cannot delete your own account.", Toast.LENGTH_LONG).show();
            return;
        }

        // prevent deleting a user with existing reservations
        ReservationRepository reservationRepository = new ReservationRepository(getActivity());
        List<Reservation> userReservations = reservationRepository.getReservationsByUser(user.getId());
        if (!userReservations.isEmpty()) {
            Toast.makeText(getActivity(), "Cannot delete user with existing reservations.", Toast.LENGTH_LONG).show();
            return;
        }

        // soft delete sets is_active to 0
        boolean success = userRepository.deleteUser(user.getId());
        if (success) {
            Toast.makeText(getActivity(), "User deleted.", Toast.LENGTH_SHORT).show();
            loadUsers();
        } else {
            Toast.makeText(getActivity(), "Failed to delete user.", Toast.LENGTH_LONG).show();
        }
    }
}