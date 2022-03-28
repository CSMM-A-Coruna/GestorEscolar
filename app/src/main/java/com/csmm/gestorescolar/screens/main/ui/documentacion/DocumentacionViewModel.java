package com.csmm.gestorescolar.screens.main.ui.documentacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DocumentacionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DocumentacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Pantalla 'Documentación'");
    }

    public LiveData<String> getText() {
        return mText;
    }
}