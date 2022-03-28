package com.csmm.gestorescolar.screens.main.ui.misdatos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisDatosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MisDatosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Mis datos'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}