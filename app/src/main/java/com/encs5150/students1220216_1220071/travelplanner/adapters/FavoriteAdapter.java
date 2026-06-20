package com.encs5150.students1220216_1220071.travelplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    // interface for click events
    public interface OnFavoriteActionListener {
        void onTripClick(Trip trip);
        void onRemoveClick(Trip trip);
        void onReserveClick(Trip trip);
    }

    private List<Trip> trips;
    private Context context;
    private OnFavoriteActionListener listener;

    public FavoriteAdapter(Context context, OnFavoriteActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.trips = new ArrayList<>();
    }

    // update the list and refresh the RecyclerView
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    // creates a new ViewHolder by having the item_favorite layout for each row
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates item_favorite.xml for each row
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    // fills each row with data from the trip at the given position in the list
    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        // fills each row with trip data
        Trip trip = trips.get(position);

        holder.destination.setText(trip.getDestination());
        holder.country.setText(trip.getCountry());
        holder.price.setText(context.getString(R.string.trip_price_value, trip.getPrice()));
        holder.rating.setText(context.getString(R.string.trip_rating_value, trip.getRating()));

        // load image using glide
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

        // remove from favorites
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveClick(trip);
            }
        });

        // open reservation form
        holder.reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReserveClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // holds references to all views in a single favorite row
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView destination;
        TextView country;
        TextView price;
        TextView rating;
        Button removeButton;
        Button reserveButton;

        // finds and stores all views from item_favorite.xml
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.favorite_trip_image);
            destination = itemView.findViewById(R.id.favorite_destination);
            country = itemView.findViewById(R.id.favorite_country);
            price = itemView.findViewById(R.id.favorite_price);
            rating = itemView.findViewById(R.id.favorite_rating);
            removeButton = itemView.findViewById(R.id.favorite_remove_button);
            reserveButton = itemView.findViewById(R.id.favorite_reserve_button);
        }
    }
}
