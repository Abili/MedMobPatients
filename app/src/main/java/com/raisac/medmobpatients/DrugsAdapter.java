package com.raisac.medmobpatients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.MyViewHolder> {
    ArrayList<Drugs> mDrugs = new ArrayList<>();
    Context mContext;

    public DrugsAdapter(ArrayList<Drugs> drugs, Context context) {
        mDrugs = drugs;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.drugslist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Drugs drugs = mDrugs.get(position);
        holder.bind(drugs);

    }

    @Override
    public int getItemCount() {
        return mDrugs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.drugName)
        TextView drugname;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Drugs drugs) {
            drugname.setText(drugs.getName());
        }
    }

}
