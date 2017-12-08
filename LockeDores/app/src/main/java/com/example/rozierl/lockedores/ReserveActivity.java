package com.example.rozierl.lockedores;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.y;
import static com.example.rozierl.lockedores.R.array.AMPM_array;
import static com.example.rozierl.lockedores.R.array.AlumniHall_array;
import static com.example.rozierl.lockedores.R.array.buildings_array;
import static com.example.rozierl.lockedores.R.array.hours_array;


public class ReserveActivity extends AppCompatActivity implements View.OnClickListener{

    //UI element declarations
    private Button bReserve;
    private Button bMainMenu;
    private Button bAvailable;
    private EditText etDate;
    private Spinner sAMPM;
    private Spinner sTime;
    private Spinner sBuilding;
    private Spinner sRooms;

    private static final String TAG = "ReserveActivity";
    private CollectionReference mColRef = FirebaseFirestore.getInstance().collection("reservations");
    private DocumentReference  mDocRef = mColRef.document("reservation");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

         mAuth = FirebaseAuth.getInstance();
         bReserve = findViewById(R.id.bReserve);
         bMainMenu = findViewById(R.id.bMainMenu);
         bAvailable = findViewById(R.id.bAvailable);
         etDate = findViewById(R.id.etDate);
         sAMPM = findViewById(R.id.sAMPM);
         sTime = findViewById(R.id.sTime);
         sBuilding = findViewById(R.id.sBuilding);
         sRooms = findViewById(R.id.sRooms);

        //Setting click listeners
        bReserve.setOnClickListener(this);
        bMainMenu.setOnClickListener(this);
        bAvailable.setOnClickListener(this);

        //Defining spinner values
        Spinner[] arr ={ sBuilding,sRooms,sAMPM,sTime};
        Integer[] arr2 ={buildings_array,AlumniHall_array,AMPM_array,hours_array};

        for (int i=0;i<arr.length;i++) {
            makeSpinner(arr[i],arr2[i]);
        }

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    public Map<String,Object> query(View v){
        String buildingText, roomText, timeText, dateText, ampm;

        buildingText =      sBuilding.getSelectedItem().toString();
        dateText =          etDate.getText().toString();
        roomText=           sRooms.getSelectedItem().toString();
        timeText =          sTime.getSelectedItem().toString();
        ampm =              sAMPM.getSelectedItem().toString();

        if(dateText.length()!=10) {
            dateText = "0000000000";
        }
        if(!checkDate(dateText)){
            Toast.makeText(ReserveActivity.this, "Enter a valid date please!",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,ReserveActivity.class));
            finish();
        }

        if(openRoomExists(buildingText,roomText,timeText, "").toString()!=null){
            Map<String,Object> Reservation = new HashMap<String, Object>();
            Reservation.put("building",buildingText);
            Reservation.put("date",dateText+timeText+ampm);
            Reservation.put("room",roomText);
            Reservation.put("open",false);

            return Reservation;

        }else{
            Toast.makeText(ReserveActivity.this, "That time slot is already reserved!",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public boolean checkDate(String dateText){
        if(Integer.parseInt(dateText.substring(0,2))>12){return false;}
        if(Integer.parseInt(dateText.substring(3,5))>31){return false;}
        else{
            return true;
        }
    }


    public Query openRoomExists(String b, String r, String d, String UiD){
        Query query =  mColRef.whereEqualTo("building",b)
                                .whereEqualTo("room",r)
                                .whereEqualTo("date",d)
                                .whereEqualTo("open",true)
                                .whereEqualTo("UiD",UiD);
        return query;
    }


    public void reserveRoom(Map<String,Object> Reservation){
        mColRef.add(Reservation).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Success!");
                    Toast.makeText(ReserveActivity.this, "Your room has been reserved!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Log.d(TAG, "Something went wrong! Please try again.");
                }
            }
        });
        startActivity(new Intent(ReserveActivity.this,ReserveActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == bReserve) {
            reserveRoom(query(v));
        }
        if (v == bMainMenu) {
            finish();
            startActivity(new Intent(this, MainMenuActivity.class));
        }
        if(v==bAvailable){
            if(query(v)!=null){
                Toast.makeText(ReserveActivity.this, "There are rooms available!",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ReserveActivity.this, "Looks like you're out of luck!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void makeSpinner(Spinner s,Integer i){
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(this,
                i, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(a);
    }
}
