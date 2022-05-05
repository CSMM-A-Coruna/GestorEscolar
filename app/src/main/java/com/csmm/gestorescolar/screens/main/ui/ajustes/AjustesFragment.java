package com.csmm.gestorescolar.screens.main.ui.ajustes;

import android.os.Bundle;

import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;

public class AjustesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);

        Preference mCuenta = getPreferenceManager().findPreference("tu_cuenta");
        Preference mCuentaSeguridad = getPreferenceManager().findPreference("seguridad");
        Preference mNotificaciones = getPreferenceManager().findPreference("notificaciones");
        Preference mRecursosAdicionales = getPreferenceManager().findPreference("recursos_adicionales");

        assert mCuenta != null;
        mCuenta.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_tucuenta);
            return true;
        });

        assert mCuentaSeguridad != null;
        mCuentaSeguridad.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_seguridad);
            return true;
        });

        assert mNotificaciones != null;
        mNotificaciones.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_notificaciones);
            return true;
        });

        assert mRecursosAdicionales != null;
        mRecursosAdicionales.setOnPreferenceClickListener(preference -> {
            Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_recursosadicionales);
            return true;
        });

        /*mVersionName = (Preference) getPreferenceManager().findPreference("version_name");
        mVersionCode = (Preference) getPreferenceManager().findPreference("version_code");

        mVersionName.setTitle("Versión");
        mVersionName.setSummary(BuildConfig.VERSION_NAME);

        mVersionCode.setTitle("Código de versión");
        mVersionCode.setSummary(String.valueOf(BuildConfig.VERSION_CODE));*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}