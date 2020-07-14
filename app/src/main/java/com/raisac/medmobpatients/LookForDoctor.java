package com.raisac.medmobpatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.geofire.GeoFire;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.raisac.medmobpatients.Auths.Profile;
import com.raisac.medmobpatients.MapsActivities.MapsActivity;

import butterknife.ButterKnife;

public class LookForDoctor extends AppCompatActivity {

    private static final String TAG = "LookForDoctor";
    private String mPatientSufering;
    private BottomSheetDialog mBottomSheetDialog;
    private String[] mDep;

    Spinner mSufferingFrom;
    String users = "users";
    String location = "location";
    String patients = "patients";

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private String mUid;
    private DatabaseReference mLocation_ref;
    private GeoFire mGeoFire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_for_doctor);
        ButterKnife.bind(this);
       mAuth = FirebaseAuth.getInstance();



    }

    public void emergncy(View view) {
//        OrderDoctor dialog = new OrderDoctor();
//        dialog.show(getSupportFragmentManager(), TAG);

        startActivity(new Intent(this, MapsActivity.class));
    }

    public void orderDoctor(View view) {
//        OrderDoctor dialog = new OrderDoctor();
//        dialog.show(getSupportFragmentManager(), TAG);
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void openProfile(View view) {
        startActivity(new Intent(this, Profile.class));
    }

    public void orderDrugs(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            startActivity(new Intent(this, MainActivity.class));
        }else {
            startActivity(new Intent(this, Pharmacy.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}

