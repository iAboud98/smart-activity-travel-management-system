package com.encs5150.students1220216_1220071.travelplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import com.encs5150.students1220216_1220071.travelplanner.repositories.FavoriteRepository;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    // interface for click events
    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private List<Trip> trips;
    private Context context;
    private OnTripClickListener listener;
    private boolean isAdminMode = false;

    public TripAdapter(Context context, OnTripClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.trips = new ArrayList<>();
    }

    // used for admin mode
    public TripAdapter(Context context, OnTripClickListener listener, boolean isAdminMode) {
        this.context = context;
        this.listener = listener;
        this.trips = new ArrayList<>();
        this.isAdminMode = isAdminMode;
    }

    // update the list and refresh the RecyclerView
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.destination.setText(trip.getDestination());
        holder.country.setText(trip.getCountry());
        holder.duration.setText(trip.getDurationDays() + " days");
        holder.price.setText("$" + (int) trip.getPrice());
        holder.rating.setText("★ " + trip.getRating());

        // load image from URL using Glide, show placeholder if URL fails
        Glide.with(context)
                .load(trip.getImageUrl())
                .placeholder(R.drawable.ic_app_logo)
                .error(R.drawable.ic_app_logo)
                .into(holder.image);

        // open trip details on click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTripClick(trip);
            }
        });

        ImageButton favoriteButton = holder.itemView.findViewById(R.id.trip_favorite_button);
        FavoriteRepository favoriteRepository = new FavoriteRepository(context);
        int currentUserId = SessionManager.getCurrentUserId(context);

        if (isAdminMode) {
            // hide favorite button in admin mode
            favoriteButton.setVisibility(View.GONE);
        } else {
            favoriteButton.setVisibility(View.VISIBLE);

            // set initial tint based on favorite state
            if (favoriteRepository.isFavorite(currentUserId, trip.getID())) {
                favoriteButton.setColorFilter(context.getResources().getColor(R.color.color_primary));
            } else {
                favoriteButton.setColorFilter(context.getResources().getColor(android.R.color.white));
            }

            // toggle favorite on click
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favoriteRepository.isFavorite(currentUserId, trip.getID())) {
                        favoriteRepository.removeFavorite(currentUserId, trip.getID());
                        favoriteButton.setColorFilter(context.getResources().getColor(android.R.color.white));
                    } else {
                        favoriteRepository.addFavorite(currentUserId, trip.getID());
                        favoriteButton.setColorFilter(context.getResources().getColor(R.color.color_primary));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView destination;
        TextView country;
        TextView duration;
        TextView price;
        TextView rating;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.trip_image);
            destination = itemView.findViewById(R.id.trip_destination);
            country = itemView.findViewById(R.id.trip_country);
            duration = itemView.findViewById(R.id.trip_duration);
            price = itemView.findViewById(R.id.trip_price);
            rating = itemView.findViewById(R.id.trip_rating);
        }
    }
}