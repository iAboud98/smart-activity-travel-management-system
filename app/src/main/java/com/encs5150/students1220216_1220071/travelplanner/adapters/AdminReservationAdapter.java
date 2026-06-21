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

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.AdminReservationViewHolder> {

    private List<Reservation> reservations;
    private Context context;

    public AdminReservationAdapter(Context context) {
        this.context = context;
        this.reservations = new ArrayList<>();
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates item_admin_reservation.xml for each row
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_reservation, parent, false);
        return new AdminReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReservationViewHolder holder, int position) {
        // fills each row with reservation data including user email and trip name from join
        Reservation reservation = reservations.get(position);

        holder.tripName.setText(reservation.getTripName());
        holder.userEmail.setText("User: " + reservation.getUserEmail());
        holder.date.setText("Date: " + reservation.getReservationDate());
        holder.type.setText(reservation.getReservationType());
        holder.quantity.setText(reservation.getQuantity() + " travelers");
        holder.status.setText(reservation.getStatus());

        if (reservation.getAdditionalInfo() != null && !reservation.getAdditionalInfo().isEmpty()) {
            holder.additionalInfo.setText("Notes: " + reservation.getAdditionalInfo());
            holder.additionalInfo.setVisibility(View.VISIBLE);
        } else {
            holder.additionalInfo.setVisibility(View.GONE);
        }

        // cancel button to cancel a user reservation
        Button cancelButton = holder.itemView.findViewById(R.id.admin_res_cancel_button);

        // hide cancel button if already cancelled
        if (reservation.getStatus().equals("Cancelled")) {
            cancelButton.setEnabled(false);
            cancelButton.setText("Cancelled");
        } else {
            cancelButton.setEnabled(true);
            cancelButton.setText("Cancel Reservation");
            final boolean[] cancelPending = {false};
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!cancelPending[0]) {
                        // first click ask for confirmation
                        cancelPending[0] = true;
                        cancelButton.setText("Confirm Cancellation");
                        Toast.makeText(context, "Tap again to confirm cancellation.", Toast.LENGTH_SHORT).show();
                    } else {
                        // second click cancel the reservation
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
    public static class AdminReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tripName;
        TextView userEmail;
        TextView date;
        TextView type;
        TextView quantity;
        TextView status;
        TextView additionalInfo;
        Button cancelButton;

        public AdminReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.admin_res_trip_name);
            userEmail = itemView.findViewById(R.id.admin_res_user_email);
            date = itemView.findViewById(R.id.admin_res_date);
            type = itemView.findViewById(R.id.admin_res_type);
            quantity = itemView.findViewById(R.id.admin_res_quantity);
            status = itemView.findViewById(R.id.admin_res_status);
            additionalInfo = itemView.findViewById(R.id.admin_res_additional_info);
            cancelButton = itemView.findViewById(R.id.admin_res_cancel_button);
        }
    }
}