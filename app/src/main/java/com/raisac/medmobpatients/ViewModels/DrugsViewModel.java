package com.raisac.medmobpatients.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.raisac.medmobpatients.Models.Drugs;
import com.raisac.medmobpatients.Repository.DrugsResository;

import java.util.List;


public class DrugsViewModel extends ViewModel {
    private MutableLiveData<List<Drugs>> mDrugs;
    private DrugsResository mDrugsResository;

    public void init(){
        if(mDrugs!=null){
            return;
        }
        mDrugsResository = new DrugsResository();
        mDrugs = mDrugsResository.getDrugs();
    }
    public LiveData<List<Drugs>> getDrugs(){
        return mDrugs;
    }
}
