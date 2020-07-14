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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raisac.medmobpatients.Adapters.DrugsAdapter;
import com.raisac.medmobpatients.Models.Drugs;
import com.raisac.medmobpatients.ViewModels.DrugsViewModel;
import com.raisac.medmobpatients.databinding.DrugslistBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Pharmacy extends AppCompatActivity {
    @BindView(R.id.searchDrugs)
    SearchView mSearchDrugs;
    @BindView(R.id.lisofDrugsrecycler)
    RecyclerView drugsList;
    @BindView(R.id.orderDrugs)
    Button order_drugs;

    ArrayAdapter mDrugs;
    DatabaseReference mDatabaseDrugs;
    private BottomSheetDialog mBottomSheetDialog;
    ArrayList<Drugs> drugs;
    private DrugsAdapter mDrugsAdapter;
    DrugsViewModel mDrugsViewModel;
    // private FirebaseRecyclerAdapter<Drugs, MyViewHolder> mFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        ButterKnife.bind(this);

        mDrugsViewModel = new ViewModelProvider(this).get(DrugsViewModel.class);
        mDrugsViewModel.init();
        mDrugsViewModel.getDrugs().observe(this, new Observer<List<Drugs>>() {
            @Override
            public void onChanged(List<Drugs> drugs) {
                mDrugsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void orderForDrugs(View view) {
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(R.layout.order_drugs);
        mBottomSheetDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDrugsLIst();

    }

    public void getDrugsLIst() {
        mDrugsAdapter = new DrugsAdapter(mDrugsViewModel.getDrugs().getValue(), Pharmacy.this);
        drugsList.setAdapter(mDrugsAdapter);
        drugsList.setLayoutManager(new GridLayoutManager(this, 3));
    }

}
