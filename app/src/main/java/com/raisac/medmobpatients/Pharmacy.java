package com.raisac.medmobpatients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Pharmacy extends AppCompatActivity {
    @BindView(R.id.searchDrugs)
    SearchView mSearchDrugs;
    @BindView(R.id.lisofDrugsrecycler)
    RecyclerView drugsList;
    @BindView(R.id.describeCondition)
    EditText condition;
    @BindView(R.id.orderDrugs)
    Button order_drugs;


    RecyclerView mdrugsReListRecycler;

    ArrayAdapter mDrugs;
    DatabaseReference mDatabaseDrugs;
    private Drugs mDrugs1 = new Drugs();
    private BottomSheetDialog mBottomSheetDialog;
    ArrayList<Drugs> drugs;
    // private FirebaseRecyclerAdapter<Drugs, MyViewHolder> mFirebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        ButterKnife.bind(this);

        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.drugs_recyclerview, null);
        mdrugsReListRecycler = view.findViewById(R.id.drugstobeSelected);
        drugs = new ArrayList<>();
        mDatabaseDrugs = FirebaseDatabase.getInstance().getReference().child("medication").child("tablets").child("0");
    }

    public void orderForDrugs(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getDrugsLIst() {
        mBottomSheetDialog.setContentView(R.layout.drugs_recyclerview);
        mBottomSheetDialog.show();


        mDatabaseDrugs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    drugs.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        for (int i=0; i<drugs.size();i++) {
                            Drugs drugslist = dss.getValue(Drugs.class);
                            drugs.add(drugslist);
                            TextView textView = findViewById(R.id.tvt);
                            textView.setText(drugs.toString());
                            //drugs.addAll((Collection<? extends Drugs>) drugs.get(i));
//                            DrugsAdapter drugsAdapter = new DrugsAdapter( drugs, Pharmacy.this);
//                            mdrugsReListRecycler.setAdapter(drugsAdapter);
//                            mdrugsReListRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }

                    }
//                    StringBuilder builder = new StringBuilder();
//                    for (int i = 0; i < drugs.size(); i++) {
//                        builder.append(drugs.get(i));
//                        drugs.add(builder.toString());
//                        // DrugsAdapter adapter = new DrugsAdapter(drugs, Pharmacy.this);
//                    }
                    //Toast.makeText(Pharmacy.this, builder.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addDrugsTolist(View view) {
        getDrugsLIst();

    }
}
