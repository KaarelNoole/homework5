package com.noole.myapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UserEditRecyclerAdapter extends RecyclerView.Adapter<UserEditRecyclerAdapter.UserviewHolder>{

    ArrayList<com.noole.myapplication.User> userArrayList;

    @NonNull
    @Override
    public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_edituser,parent,false);
        return new UserviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserviewHolder holder, int position) {
        com.noole.myapplication.User user = userArrayList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.email.setText(user.getEmail());
    }

    public UserEditRecyclerAdapter() {
        this.userArrayList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public void updateUserList(final ArrayList<com.noole.myapplication.User> userArrayList) {
        this.userArrayList = userArrayList;
        notifyDataSetChanged();
    }



    static class UserviewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText firstName;
        private final TextInputEditText lastName;
        private final TextInputEditText email;

        public UserviewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.Fname_EditUser);
            lastName = itemView.findViewById(R.id.Lname_EditUser);
            email = itemView.findViewById(R.id.Email_EditUser);
        }
    }
}