package com.noole.myapplication;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserEditViewModel extends AndroidViewModel {

    private final com.noole.myapplication.AuthRepository authRepository;
    public final MutableLiveData<FirebaseUser> userMutableLiveData;
    public final MutableLiveData<Boolean> loggedOutMutableLiveData;
    public final MutableLiveData<ArrayList<com.noole.myapplication.User>> userLiveData;

    public UserEditViewModel(@NonNull Application application) {
        super(application);
        authRepository = new com.noole.myapplication.AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        loggedOutMutableLiveData = authRepository.getLoggedOutMutableLiveData();
        userLiveData = authRepository.getUserLiveData();
    }



    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }

    public void UpdateUserInformation(String firstName, String lastName, String email) {
        authRepository.UserInformationUpdate(firstName,lastName,email);
    }

    public MutableLiveData<ArrayList<com.noole.myapplication.User>> getUserLiveData() {
        return userLiveData;
    }
}
