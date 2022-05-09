package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle.tucuenta_detalle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TuCuenta_InfoFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_tucuenta_info, rootKey);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String usuario = sharedPref.getString("usuario",null);
        String nombre = sharedPref.getString("nombre", null);
        String apellido1 = sharedPref.getString("apellido1", null);
        String apellido2 = sharedPref.getString("apellido2", null);
        String nombreCompleto = String.format("%s %s %s", nombre, apellido1, apellido2);
        String nacimiento = sharedPref.getString("nacimiento", null);
        String dni = sharedPref.getString("dni", null);
        String accesos = String.valueOf(sharedPref.getInt("accesos", 0));
        String alumnosJson = sharedPref.getString("alumnosAsociados", null);

        StringBuilder alumnos = new StringBuilder();
        alumnos.append("");
        try {
            JSONArray jsonAlumnos = new JSONArray(alumnosJson);
            for(int i=0; i<jsonAlumnos.length(); i++) {
                JSONObject json = jsonAlumnos.getJSONObject(i);
                if(!alumnos.toString().equals("")) {
                    alumnos.append(", ");
                }
                alumnos.append(json.getString("nombre"));
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        Preference mUsuario = getPreferenceManager().findPreference("usuario");
        Preference mNombreCompleto = getPreferenceManager().findPreference("nombre_completo");
        Preference mNacimiento = getPreferenceManager().findPreference("fecha_nacimiento");
        Preference mDni = getPreferenceManager().findPreference("dni");
        Preference mAccesos = getPreferenceManager().findPreference("accesos");
        Preference mAlumnosAsociados = getPreferenceManager().findPreference("alumnos");

        assert mUsuario != null;
        mUsuario.setSummary(usuario);
        assert mNombreCompleto != null;
        mNombreCompleto.setSummary(nombreCompleto);
        assert mNacimiento != null;
        mNacimiento.setSummary(nacimiento);
        assert mDni != null;
        mDni.setSummary(dni);
        assert mAccesos != null;
        mAccesos.setSummary(accesos);
        assert mAlumnosAsociados != null;
        mAlumnosAsociados.setSummary(alumnos);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

