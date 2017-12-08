package com.example.rozierl.lockedores;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.rozierl.lockedores.R.id.bLogin;
import static com.example.rozierl.lockedores.R.id.bProfile;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainMenuActivity";
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button bProfile;
    private Button bReserve;
    private Button bLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
                }else{
                }
            }
        };

        bProfile = findViewById(R.id.bProfile);
        bReserve =  findViewById(R.id.bReserve);
        bLogout =  findViewById(R.id.bLogout);

        bProfile.setOnClickListener(this);
        bReserve.setOnClickListener(this);
        bLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        if(v==bProfile){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }else if(v==bReserve){
            finish();
            startActivity(new Intent(this, ReserveActivity.class));
        }else if(v==bLogout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
