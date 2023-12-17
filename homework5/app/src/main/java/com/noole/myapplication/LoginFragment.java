package com.noole.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputEditText userEmail;
    private TextInputEditText userPassword;
    private UserViewModel userViewModel;

    private com.noole.myapplication.LoginRegisterViewModel loginRegisterViewModel;

    public static boolean isAnyStringNullOrEmpty(String... strings) {
        for (String s : strings)
            if (s == null || s.isEmpty())
                return true;
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRegisterViewModel = new ViewModelProvider(this).get(com.noole.myapplication.LoginRegisterViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        loginRegisterViewModel.getUserMutableLiveData().observe(this,firebaseUser -> {
            if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                loginRegisterViewModel.VerifyUserDocumentExistence();
                if (getView() != null) { Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_userFragment); }
            } else {
                Toast.makeText(getActivity(), "Login failed, Verify email", Toast.LENGTH_SHORT).show();
                userViewModel.logOut();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_login,container,false);

        requireActivity().setTitle("Login");


        userEmail = view.findViewById(R.id.emailText);
        userPassword = view.findViewById(R.id.passwordText);

        view.findViewById(R.id.btnLogin).setOnClickListener(view1 -> {
             String email = Objects.requireNonNull(userEmail.getText()).toString().trim();
             String password = Objects.requireNonNull(userPassword.getText()).toString().trim();
            if (isAnyStringNullOrEmpty(email, password)) {
              //snackbar that says "Please enter all fields"
                Snackbar.make(view,"Please enter all fields",Snackbar.LENGTH_SHORT).show();
             }else{
                loginRegisterViewModel.login(email,password);
            }
        });

    view.findViewById(R.id.registertextView).setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        view.findViewById(R.id.btnResetPassword).setOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_resetpw);
        });

        return view;

    }



}
