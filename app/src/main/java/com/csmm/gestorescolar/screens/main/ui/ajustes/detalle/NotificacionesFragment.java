package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.UpdatePreferenceResponseHandler;

import java.util.ArrayList;
import java.util.List;


public class NotificacionesFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_notificaciones, rootKey);

        // Hacemos una lista de todas las preferencias del XML
        List<SwitchPreferenceCompat> arrayPreferences = new ArrayList<>();
        arrayPreferences.add(getPreferenceManager().findPreference("not_comunicaciones_push"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_calificaciones_push"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_entrevistas_push"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_extraescolares_push"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_enfermeria_push"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_comunicaciones_email"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_calificaciones_email"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_entrevistas_email"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_extraescolares_email"));
        arrayPreferences.add(getPreferenceManager().findPreference("not_enfermeria_email"));

        // Chequeamos antes de nada si hay cambios en el sharedPreferences
        arrayPreferences.forEach(this::checkSharedPreferences);
        // Creamos un listener cada vez que el switch cambia de estado para cada elemento de la lista
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

    private void checkSharedPreferences(SwitchPreferenceCompat preference) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        preference.setChecked(sharedPreferences.getBoolean(preference.getKey(), false));
    }

    // Cuando se detecta un cambio, se actualiza el servidor
    private void setUpOnChangeListener(SwitchPreferenceCompat preference) {
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
