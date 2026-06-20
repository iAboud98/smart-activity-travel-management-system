package com.encs5150.students1220216_1220071.travelplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.encs5150.students1220216_1220071.travelplanner.R;
import com.encs5150.students1220216_1220071.travelplanner.models.Reservation;

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

        public AdminReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.admin_res_trip_name);
            userEmail = itemView.findViewById(R.id.admin_res_user_email);
            date = itemView.findViewById(R.id.admin_res_date);
            type = itemView.findViewById(R.id.admin_res_type);
            quantity = itemView.findViewById(R.id.admin_res_quantity);
            status = itemView.findViewById(R.id.admin_res_status);
            additionalInfo = itemView.findViewById(R.id.admin_res_additional_info);
        }
    }
}