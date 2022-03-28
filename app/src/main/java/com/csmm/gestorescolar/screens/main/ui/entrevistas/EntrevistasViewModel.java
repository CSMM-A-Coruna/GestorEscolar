package com.csmm.gestorescolar.screens.main.ui.entrevistas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntrevistasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EntrevistasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Entrevistas'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}