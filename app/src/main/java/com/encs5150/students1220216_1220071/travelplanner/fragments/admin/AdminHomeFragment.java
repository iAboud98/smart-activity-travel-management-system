package com.encs5150.students1220216_1220071.travelplanner.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

public class AdminHomeFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(getString(R.string.admin_home_description) + "\n\nSigned in as: " + SessionManager.getCurrentUserEmail(getActivity()));
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16);
        return textView;
    }
}