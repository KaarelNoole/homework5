package com.noole.myapplication;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class EditUserInfoFragment extends Fragment {

    private com.noole.myapplication.UserEditViewModel userEditViewModel;
    private com.noole.myapplication.UserEditRecyclerAdapter userEditRecyclerAdapter;
    NavController navController;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEditViewModel = new ViewModelProvider(this).get(com.noole.myapplication.UserEditViewModel.class);
        userEditViewModel.getUserLiveData().observe(this, userArrayList ->  userEditRecyclerAdapter.updateUserList(userArrayList));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("User information editing");
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_edit_user_info, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.UserEditRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userEditRecyclerAdapter = new com.noole.myapplication.UserEditRecyclerAdapter();
        recyclerView.setAdapter(userEditRecyclerAdapter);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TextInputEditText FnameEditText = getView().findViewById(R.id.Fname_EditUser);
        TextInputEditText LnameEditText = getView().findViewById(R.id.Lname_EditUser);
        TextInputEditText EmailEditText = getView().findViewById(R.id.Email_EditUser);

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String TAG = "sss";

        if (item.getItemId() == R.id.Edit_User_info) {
            String Fname = Objects.requireNonNull(FnameEditText).getText().toString().trim();
            String Lname = Objects.requireNonNull(LnameEditText).getText().toString().trim();
            String Email = Objects.requireNonNull(EmailEditText).getText().toString().trim();
            Log.i(TAG, "Email: "+Email);

            if (Email.matches(emailPattern)
                    && Fname.length() > 0
                    && Lname.length() > 0) {

                userEditViewModel.UpdateUserInformation(Fname,Lname,Email);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        navController.navigate(R.id.action_editUserInfoFragment_to_loginFragment);
                    }
                }, 2500);

            } else {
                if (Fname.equals("")) {
                    Toast.makeText(getActivity(), "Fill out first name", Toast.LENGTH_SHORT).show();
                }if (Lname.equals("")) {
                    Toast.makeText(getActivity(), "Fill out last name", Toast.LENGTH_SHORT).show();
                }if (!Email.matches(emailPattern)) {
                    Toast.makeText(getActivity(), "Must be a valid email", Toast.LENGTH_SHORT).show();
                }
                navController.navigate(R.id.action_editUserInfoFragment_to_userFragment);
            }
        }
        return super.onOptionsItemSelected(item);
    }


}