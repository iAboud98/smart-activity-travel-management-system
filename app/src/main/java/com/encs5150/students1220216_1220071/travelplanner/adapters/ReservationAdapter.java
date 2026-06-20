package com.encs5150.students1220216_1220071.travelplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;
import com.encs5150.students1220216_1220071.travelplanner.repositories.ReservationRepository;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    // interface for click events
    public interface OnReservationClickListener {
        void onReservationClick(Reservation reservation);
    }

    private List<Reservation> reservations;
    private Context context;
    private OnReservationClickListener listener;

    public ReservationAdapter(Context context, OnReservationClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.reservations = new ArrayList<>();
    }

    // update the list and refresh the RecyclerView
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    // creates a new ViewHolder by having the item_reservation layout for each row
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    // fills each row with data from the reservation at the given position in the list
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);

        holder.tripName.setText(reservation.getTripName());
        holder.date.setText("Date: " + reservation.getReservationDate());
        holder.type.setText(reservation.getReservationType());
        holder.quantity.setText(reservation.getQuantity() + " travelers");
        holder.status.setText(reservation.getStatus());

        // only show additional info if not empty
        if (reservation.getAdditionalInfo() != null && !reservation.getAdditionalInfo().isEmpty()) {
            holder.additionalInfo.setText("Notes: " + reservation.getAdditionalInfo());
            holder.additionalInfo.setVisibility(View.VISIBLE);
        } else {
            holder.additionalInfo.setVisibility(View.GONE);
        }

        // open trip details on click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReservationClick(reservation);
            }
        });

        Button cancelButton = holder.itemView.findViewById(R.id.reservation_cancel_button);

        // tracks if cancel button was already clicked once for this reservation
        final boolean[] cancelPending = {false};

        if (reservation.getStatus().equals("Cancelled")) {
            cancelButton.setEnabled(false);
            cancelButton.setText("Cancelled");
        } else {
            cancelButton.setEnabled(true);
            cancelButton.setText("Cancel Reservation");
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!cancelPending[0]) {
                        // first click, ask for confirmation
                        cancelPending[0] = true;
                        cancelButton.setText("Confirm Cancellation");
                        Toast.makeText(context, "Tap again to confirm cancellation.", Toast.LENGTH_SHORT).show();
                    } else {
                        // second click, cancel the reservation
                        ReservationRepository repo = new ReservationRepository(context);
                        boolean success = repo.cancelReservation(reservation.getId());
                        if (success) {
                            reservation.setStatus("Cancelled");
                            cancelButton.setEnabled(false);
                            cancelButton.setText("Cancelled");
                            holder.status.setText("Cancelled");
                            Toast.makeText(context, "Reservation cancelled.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to cancel reservation.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    // holds references to all views in a single reservation row
    // avoids calling findViewById repeatedly for every row
    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tripName;
        TextView date;
        TextView type;
        TextView quantity;
        TextView status;
        TextView additionalInfo;
        Button cancelButton;

        // finds and stores all views from item_reservation.xml
        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.reservation_trip_name);
            date = itemView.findViewById(R.id.reservation_date);
            type = itemView.findViewById(R.id.reservation_type);
            quantity = itemView.findViewById(R.id.reservation_quantity);
            status = itemView.findViewById(R.id.reservation_status);
            additionalInfo = itemView.findViewById(R.id.reservation_additional_info);
            cancelButton = itemView.findViewById(R.id.reservation_cancel_button);
        }
    }
}