package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.BuildConfig;
import com.csmm.gestorescolar.R;


public class RecursosAdicionalesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_recursosadicionales, rootKey);

        Preference mVersionName = getPreferenceManager().findPreference("version_name");
        Preference condicionesUso = getPreferenceManager().findPreference("condiciones_de_uso");
        Preference politicaPrivacidad = getPreferenceManager().findPreference("politica_privacidad");
        Preference avisoLegal = getPreferenceManager().findPreference("avisos_legales");

        assert mVersionName != null;
        mVersionName.setSummary(BuildConfig.VERSION_NAME);

        assert condicionesUso != null;
        condicionesUso.setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse("https://www.santamariadelmar.es/?page_id=19781"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return false;
        });

        assert avisoLegal != null;
        avisoLegal.setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse("https://www.santamariadelmar.es/?page_id=19781"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return false;
        });

        assert politicaPrivacidad != null;
        politicaPrivacidad.setOnPreferenceClickListener(preference -> {
            Uri uri = Uri.parse("https://www.santamariadelmar.es/?page_id=19782"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return false;
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
