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
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import java.util.ArrayList;
import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.UserViewHolder> {

    // interface for delete button click
    public interface OnUserDeleteListener {
        void onDeleteClick(User user);
    }

    private List<User> users;
    private Context context;
    private OnUserDeleteListener deleteListener;

    // constructor without delete listener used in view users screen
    public AdminUserAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
        this.deleteListener = null;  // no delete btn shown
    }

    // constructor with delete listener used in delete users screen
    public AdminUserAdapter(Context context, OnUserDeleteListener deleteListener) {
        this.context = context;
        this.users = new ArrayList<>();
        this.deleteListener = deleteListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflates item_admin_user.xml for each row
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // fills each row with user data
        User user = users.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.email.setText("Email: " + user.getEmail());
        holder.phone.setText("Phone: " + user.getPhone());
        holder.gender.setText("Gender: " + user.getGender());
        holder.category.setText("Category: " + user.getCategory());

        if (deleteListener != null) {
            holder.deleteButton.setVisibility(View.VISIBLE);

            // tracks if delete was already clicked once for this row
            final boolean[] deletePending = {false};

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!deletePending[0]) {
                        // first click, ask for confirmation
                        deletePending[0] = true;
                        holder.deleteButton.setText("Confirm Deletion");
                        Toast.makeText(context, "Tap again to confirm deleting " + user.getFirstName() + ".", Toast.LENGTH_SHORT).show();
                    } else {
                        // second click, delete the user
                        deleteListener.onDeleteClick(user);
                    }
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // holds references to all views in a single user row
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;
        TextView gender;
        TextView category;
        Button deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            phone = itemView.findViewById(R.id.user_phone);
            gender = itemView.findViewById(R.id.user_gender);
            category = itemView.findViewById(R.id.user_category);
            deleteButton = itemView.findViewById(R.id.user_delete_button);
        }
    }
}