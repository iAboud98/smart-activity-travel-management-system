package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

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
import com.encs5150.students1220216_1220071.travelplanner.adapters.AdminUserAdapter;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.repositories.UserRepository;
import java.util.List;

public class AdminViewUsersFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_view_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        UserRepository userRepository = new UserRepository(getActivity());
        TextView emptyState = getActivity().findViewById(R.id.users_empty_state);

        // setup RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AdminUserAdapter adapter = new AdminUserAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        // load all regular users
        List<User> users = userRepository.getAllUsers();
        adapter.setUsers(users);

        if (users.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }
}