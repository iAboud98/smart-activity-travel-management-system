package com.encs5150.students1220216_1220071.travelplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class AdminTripAdapter extends RecyclerView.Adapter<AdminTripAdapter.AdminTripViewHolder> {

    // interface for edit and delete button clicks
    public interface OnAdminTripActionListener {
        void onEditClick(Trip trip);
        void onDeleteClick(Trip trip);
    }

    private List<Trip> trips;
    private Context context;
    private OnAdminTripActionListener listener;

    public AdminTripAdapter(Context context, OnAdminTripActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.trips = new ArrayList<>();
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates item_admin_trip.xml for each row
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_trip, parent, false);
        return new AdminTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTripViewHolder holder, int position) {
        // fills each row with trip data
        Trip trip = trips.get(position);

        holder.destination.setText(trip.getDestination());
        holder.country.setText(trip.getCountry());
        holder.price.setText("$" + (int) trip.getPrice());
        holder.rating.setText("★ " + trip.getRating());
        holder.duration.setText(trip.getDurationDays() + " days");

        // edit button opens the edit form with this trip data
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClick(trip);
            }
        });

        // delete button click - will be implemented in B20
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // holds references to all views in a single trip row
    public static class AdminTripViewHolder extends RecyclerView.ViewHolder {
        TextView destination;
        TextView country;
        TextView price;
        TextView rating;
        TextView duration;
        Button editButton;
        Button deleteButton;

        public AdminTripViewHolder(@NonNull View itemView) {
            super(itemView);
            destination = itemView.findViewById(R.id.admin_trip_destination);
            country = itemView.findViewById(R.id.admin_trip_country);
            price = itemView.findViewById(R.id.admin_trip_price);
            rating = itemView.findViewById(R.id.admin_trip_rating);
            duration = itemView.findViewById(R.id.admin_trip_duration);
            editButton = itemView.findViewById(R.id.admin_trip_edit_button);
            deleteButton = itemView.findViewById(R.id.admin_trip_delete_button);
        }
    }
}