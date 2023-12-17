package com.noole.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

// when user is logged in
public class UserViewModel extends AndroidViewModel {


    private final com.noole.myapplication.AuthRepository authRepository;
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final MutableLiveData<ArrayList<com.noole.myapplication.User>> userLiveData;
    public UserViewModel(@NonNull Application application) {
        super(application);
        authRepository = new com.noole.myapplication.AuthRepository(application);
        userMutableLiveData = authRepository.getUserMutableLiveData();
        loggedOutMutableLiveData = authRepository.getLoggedOutMutableLiveData();
        userLiveData = authRepository.getUserLiveData();
    }

    public void logOut() { authRepository.logOut(); }



    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
    public MutableLiveData<Boolean> getLoggedOutMutableLiveData() {
        return loggedOutMutableLiveData;
    }
    public MutableLiveData<ArrayList<com.noole.myapplication.User>> getUserLiveData() {
        return userLiveData;
    }
}
