package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComunicacionesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ComunicacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Comunicaciones'");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setmText(String texto) {
        mText.setValue(texto);
    }
}