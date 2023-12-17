package com.noole.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends Fragment {
    //declare resetpw button
    private Button resetpw;
    private TextInputEditText email;
    private static final String TAG = "pw reset thing:";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("forgot pw");
        View view = inflater.inflate(R.layout.fragment_resetpw,container,false);
        Toast.makeText(getActivity(),"this", Toast.LENGTH_SHORT).show();
        //initialize resetpw button
        resetpw = view.findViewById(R.id.btnResetPW);
        email = view.findViewById(R.id.textEmail);
        //set onclick listener for resetpw button
        resetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get email from text field
                String emailText = email.getText().toString();
                Log.i(TAG, "onClick: "+emailText);
                //send email to firebase
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailText)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    Navigation.findNavController(view).navigate(R.id.action_resetpw_to_loginFragment);

                                }
                            }
                        });
            }}
        );
        return view;
    }

    private boolean isValidEmail(String toString) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(toString).matches();
    }


}