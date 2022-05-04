package com.csmm.gestorescolar.screens.main.ui.ajustes;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;

public class AjustesFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}