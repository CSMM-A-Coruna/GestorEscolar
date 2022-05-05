package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.databinding.AjustesRecursosadicionalesFragmentBinding;

public class TuCuentaFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_tucuenta, rootKey);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
