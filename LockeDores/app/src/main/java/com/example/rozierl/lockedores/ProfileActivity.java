package com.example.rozierl.lockedores;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import static com.example.rozierl.lockedores.R.id.bReserve;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ProfileActivity";
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("users/reservations");
    private CollectionReference mColRef = FirebaseFirestore.getInstance().collection("reservations");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button bLogout;
    private Button bMainMenu;
    private TextView tvReservation;


    @Override
    protected void onStart(){
        super.onStart();
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String building = documentSnapshot.getString("building");
                            String room = documentSnapshot.getString("room");
                            String date = documentSnapshot.getString("date");
                            Integer day = Integer.parseInt(date.substring(3,5));
                            Integer month = Integer.parseInt(date.substring(0,2));
                            Integer year = Integer.parseInt(date.substring(6,10));
                            Integer time = ((Integer.parseInt(date.substring(10,11))));
                            String am = date.substring(11,12);
                            tvReservation.setText("You have "
                                                    +room+
                                                  " in "+building+
                                                  " reserved on the "+day+" day in the "+month+
                                                    " month of the "+" year "+year+
                                                  " from "+time+am+ " to "+ time+1+am);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }else{
                }
            }
        };

        bLogout =  findViewById(R.id.bLogout);
        bMainMenu =  findViewById(R.id.bMainMenu);
        bLogout.setOnClickListener(this);
        bMainMenu.setOnClickListener(this);
        tvReservation = findViewById(R.id.tvReservation);

    }

    @Override
    public void onClick(View v){
        if(v==bLogout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }else if(v==bMainMenu) {
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
        }
    }
}