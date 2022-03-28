package com.csmm.gestorescolar.screens.main.ui.cerrarsesion;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csmm.gestorescolar.screens.auth.LoginActivity;

public class CerrarsesionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public CerrarsesionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Calificaciones'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}