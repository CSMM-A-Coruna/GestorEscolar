package com.csmm.gestorescolar.screens.main.ui.horario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HorarioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HorarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'horario'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}