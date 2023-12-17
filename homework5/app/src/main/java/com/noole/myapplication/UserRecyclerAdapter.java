package com.noole.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

//custom adapter class for recyclerview
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.UserViewHolder>{
    ArrayList<User> userArrayList;

    public UserRecyclerAdapter() {
        this.userArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user,parent,false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.email.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public void updateUserList(final ArrayList<User> userArrayList){
        this.userArrayList = userArrayList;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        private final TextInputEditText firstName;
        private final TextInputEditText lastName;
        private final TextInputEditText email;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstText);
            lastName = itemView.findViewById(R.id.textEmail);
            email = itemView.findViewById(R.id.emailText);
        }
    }
}
