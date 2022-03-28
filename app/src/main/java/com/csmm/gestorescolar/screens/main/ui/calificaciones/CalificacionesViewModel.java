package com.csmm.gestorescolar.screens.main.ui.calificaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalificacionesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalificacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Calificaciones'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}