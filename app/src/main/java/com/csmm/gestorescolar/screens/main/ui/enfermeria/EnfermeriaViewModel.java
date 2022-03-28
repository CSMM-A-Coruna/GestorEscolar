package com.csmm.gestorescolar.screens.main.ui.enfermeria;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EnfermeriaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EnfermeriaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Enfermeria'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}