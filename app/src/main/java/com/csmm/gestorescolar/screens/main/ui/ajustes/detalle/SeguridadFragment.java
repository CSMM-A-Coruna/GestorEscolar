package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.UpdatePreferenceResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class SeguridadFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_seguridad, rootKey);

        List<SwitchPreferenceCompat> arrayPreferences = new ArrayList<>();

        arrayPreferences.add(getPreferenceManager().findPreference("autentificacion_dos_fases"));
        arrayPreferences.add(getPreferenceManager().findPreference("proteccion_restablecimiento"));

        arrayPreferences.forEach(this::setUpOnChangeListener);

    }


    // Petición al servidor
    private void updatePreferencesOnServer(String preference, boolean value) {
        RestClient.getInstance(requireContext()).updatePreference(preference, value, new UpdatePreferenceResponseHandler() {
            @Override
            public void requestDidComplete() {
                System.out.println("ok");
            }

            @Override
            public void requestDidFail(int statusCode) {

            }
        });
    }

    // Cuando se detecta un cambio, se actualiza el servidor
    private void setUpOnChangeListener(Preference preference) {
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            updatePreferencesOnServer(preference1.getKey(), (Boolean) newValue);
            return true;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

