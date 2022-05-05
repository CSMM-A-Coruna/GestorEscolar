package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.csmm.gestorescolar.BuildConfig;
import com.csmm.gestorescolar.R;


public class RecursosAdicionalesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_recursosadicionales, rootKey);

        Preference mVersionName = getPreferenceManager().findPreference("version_name");
        mVersionName.setSummary(BuildConfig.VERSION_NAME);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
