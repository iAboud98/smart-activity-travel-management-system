package com.encs5150.students1220216_1220071.travelplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.encs5150.students1220216_1220071.travelplanner.R;

abstract class BasePlaceholderFragment extends Fragment {

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        TextView titleView = view.findViewById(R.id.placeholder_title);
        TextView descriptionView = view.findViewById(R.id.placeholder_description);

        titleView.setText(getTitleResId());
        descriptionView.setText(getDescriptionResId());

        return view;
    }

    protected abstract int getTitleResId();

    protected abstract int getDescriptionResId();
}
