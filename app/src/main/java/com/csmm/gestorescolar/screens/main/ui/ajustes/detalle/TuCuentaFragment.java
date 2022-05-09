package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;

public class TuCuentaFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_tucuenta, rootKey);

        Preference mInfo = getPreferenceManager().findPreference("info_cuenta");
        Preference mCambioContraseña = getPreferenceManager().findPreference("cambio_contraseña");

        assert mInfo != null;
        mInfo.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_tucuenta_info);
            return true;
        });

        assert mCambioContraseña != null;
        mCambioContraseña.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_tucuenta_cambiocontraseña);
            return true;
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
