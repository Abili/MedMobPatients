package com.raisac.medmobpatients.Repository;

import androidx.lifecycle.MutableLiveData;

import com.raisac.medmobpatients.Models.Drugs;
import com.raisac.medmobpatients.R;

import java.util.ArrayList;
import java.util.List;

public class DrugsResository  {
    private static DrugsResository instance;
    private ArrayList<Drugs> dataSet = new ArrayList<>();
    public static DrugsResository getInstance(){
        if(instance ==null){
            instance = new DrugsResository();
        }
        return instance;
    }
    public MutableLiveData<List<Drugs>> getDrugs(){
        setDrugs();
        MutableLiveData<List<Drugs>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    //sample data, which later will be got from webservice
    private void setDrugs() {
        dataSet.add(new Drugs("Apirine", "3500", R.drawable.aspirin));
        dataSet.add(new Drugs("Hp_cough", "3500", R.drawable.hpcough));
        dataSet.add(new Drugs("Pandadol Extra", "4500", R.drawable.panadol_extra));
        dataSet.add(new Drugs("Magnesium", "2500", R.drawable.magnesium));
        dataSet.add(new Drugs("Drug 3", "3500",  R.drawable.drug_3));
        dataSet.add(new Drugs("Vitamine C", "15000", R.drawable.vit_c));
        dataSet.add(new Drugs("Morphine", "25000", R.drawable.morphine));
        dataSet.add(new Drugs("Predinesone", "10500", R.drawable.prednisone));
        dataSet.add(new Drugs("Syringe", "5000", R.drawable.syringe));
        dataSet.add(new Drugs("Torex", "31500", R.drawable.torex));

    }
}
