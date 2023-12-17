package com.noole.myapplication;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//repository class for db + auth
public class AuthRepository {
    //log tag
    private static final String TAG = "Firebase:";
    //firebase variables
    private FirebaseAuth mAuth;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    //mutablelivedata variables
    private final MutableLiveData<FirebaseUser> userMutableLiveData;
    private final MutableLiveData<Boolean> loggedOutMutableLiveData;
    private final MutableLiveData<ArrayList<com.noole.myapplication.User>> userLiveData;
    private final ArrayList<com.noole.myapplication.User> userArrayList = new ArrayList<>();
    private final Application application;

    public AuthRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        loggedOutMutableLiveData = new MutableLiveData<>();
        userLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null){
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutMutableLiveData.postValue(false);
            loadUserData();
        }
    }

    //method for registering user
    public void userRegistration(String firstName, String lastName, String email, String password){
        //creating a user with email + password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.getMainExecutor(), task->{
                    //if user was created successfully then save data to firebase firestore
                    if (task.isSuccessful()){
                        if (firebaseAuth.getCurrentUser() != null){
                            //gets the newly created users UID
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            //creates new collection named users if one doesn't exist into it add a new document with UID reference
                            DocumentReference documentReference = db.collection("users").document(userId);
                            //this is the data that will be written into the document
                            Map<String,Object> user = new HashMap<>();
                            user.put("firstname",firstName);
                            user.put("lastname",lastName);
                            user.put("email",email);
                            documentReference.set(user)
                                    .addOnSuccessListener(aVoid -> Log.i(TAG, "onSuccess: user data was saved"))
                                    .addOnFailureListener(e -> Log.e(TAG, "onFailure: Error writing to DB document", e));
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());


                            mAuth = FirebaseAuth.getInstance();
                            FirebaseUser VerUser = mAuth.getCurrentUser();

                            VerUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                                Toast.makeText(application, "Email sent. Please verify email", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException().getMessage())
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //method for user logout
    public void logOut(){
        firebaseAuth.signOut();
        loggedOutMutableLiveData.postValue(true);
    }

    //method for registering verified users
    public void verifiedUserRegistration() {
        final String[] VerFName = new String[1];
        final String[] VerLName = new String[1];
        final String[] VerEmail = new String[1];


        if (firebaseAuth.getCurrentUser() != null) {
            //gets newly created users UID
            String userId = firebaseAuth.getCurrentUser().getUid();

            Log.i(TAG, "onComplete: 0");
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("users").document(userId);
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                        } else {
                            Log.d(TAG, "Document does not exist!");
                            DocumentReference docRef = db.collection("unVerifiedUsers").document(userId);
                            Log.i(TAG, "onComplete: 1");
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.i(TAG, "onComplete: 1.5");
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.i(TAG, "onComplete: 2");
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            VerFName[0] = document.get("firstName").toString();
                                            VerLName[0] = document.get("lastName").toString();
                                            VerEmail[0] = document.get("email").toString();
                                            Log.i(TAG, "Fn:"+VerFName);
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                            Log.i(TAG, "onComplete: 3");


                            final String firstname = VerFName[0];
                            final String lastname = VerLName[0];
                            final String email = VerEmail[0];



                            Log.i(TAG, "onComplete: 4 > "+firstname+" "+lastname+" "+email);
                            //creates new collection named users if one doesnt exist into it add a new document with UID reference
                            DocumentReference documentReference = db.collection("users").document(userId);
                            //this is the data that will be written into the document
                            Map<String,Object> user = new HashMap<>();
                            user.put("firstName",firstname);
                            user.put("lastName",lastname);
                            user.put("email",email);
                            documentReference.set(user).addOnSuccessListener(aVoid -> Log.i(TAG, "onSuccess: user data was saved"))
                                    .addOnFailureListener(e -> Log.e(TAG, "onFaliure: Error writing to DB document", e));
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            Log.i(TAG, "onComplete: 5"); //gets trough all of this yet no user gets created
                            //note: a user gets created but only after about 3 minutes and all fields are null, why...
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                }
            });
        }
    };

    public void UserInformationUpdate(String firstname, String lastname, String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("users").document(firebaseAuth.getInstance().getCurrentUser().getUid())
                .update(
                        "firstName",firstname,
                        "lastName",lastname

                );
        if (!firebaseAuth.getCurrentUser().getEmail().equals(email)) {

            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });
            db.collection("users").document(firebaseAuth.getInstance().getCurrentUser().getUid())
                    .update(
                            "email",email
                    );
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                }
            }, 1500);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(application, "Due to an update in the user information you have been logged out", Toast.LENGTH_LONG).show();
                logOut();
            }
        }, 2000);

    }

    //method for user log in with email + password
    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()){
                        userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                    }else{
                        Toast.makeText(application, application.getString(R.string.error, task.getException()
                                        .getMessage())
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //method for obtaining user data from db
    public void loadUserData(){
        if (firebaseAuth.getCurrentUser() != null){
            String uid = firebaseAuth.getCurrentUser().getUid();
            DocumentReference doc = db.collection("users").document(uid);
            doc.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        com.noole.myapplication.User user = documentSnapshot.toObject(com.noole.myapplication.User.class);
                        userArrayList.add(user);
                        userLiveData.setValue(userArrayList);
                    }).addOnFailureListener(e ->
                    Toast.makeText(application, application.getString(R.string.error,e.getMessage()), Toast.LENGTH_SHORT).show());
        }
    }
    //getters for livedata
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




