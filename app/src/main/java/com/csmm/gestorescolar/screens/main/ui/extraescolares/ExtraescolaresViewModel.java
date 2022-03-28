package com.csmm.gestorescolar.screens.main.ui.extraescolares;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExtraescolaresViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExtraescolaresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Extraescolares'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}