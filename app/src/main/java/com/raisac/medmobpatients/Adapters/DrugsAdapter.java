package com.raisac.medmobpatients.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.raisac.medmobpatients.Models.Drugs;
import com.raisac.medmobpatients.R;
import com.raisac.medmobpatients.databinding.DrugslistBinding;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.BinginHolder> {
    List<Drugs> mDrugs;
    Context mContext;

    public DrugsAdapter(List<Drugs> drugs, Context context) {
        mDrugs = drugs;
        mContext = context;
    }

    @NonNull
    @Override
    public BinginHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DrugslistBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext), R.layout.drugslist, parent, false);
        return new BinginHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BinginHolder holder, int position) {
        Drugs drugs = mDrugs.get(position);
        holder.mBinding.setDrugs(drugs);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return mDrugs.size();
    }

    public class BinginHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.drugName)
        DrugslistBinding mBinding;


        public BinginHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

    }
}
