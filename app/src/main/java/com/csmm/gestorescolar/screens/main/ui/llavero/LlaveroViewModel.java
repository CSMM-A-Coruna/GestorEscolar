package com.csmm.gestorescolar.screens.main.ui.llavero;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LlaveroViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LlaveroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Llavero'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}