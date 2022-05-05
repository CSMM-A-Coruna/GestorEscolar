package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import com.csmm.gestorescolar.R;


public class NotificacionesFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_notificaciones, rootKey);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
