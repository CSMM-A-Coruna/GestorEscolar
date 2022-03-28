package com.csmm.gestorescolar.screens.main.ui.diario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiarioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DiarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Diario'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}