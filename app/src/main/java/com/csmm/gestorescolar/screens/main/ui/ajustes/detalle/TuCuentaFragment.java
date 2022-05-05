package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.databinding.AjustesRecursosadicionalesFragmentBinding;

public class TuCuentaFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_tucuenta, rootKey);

        Preference mInfo = getPreferenceManager().findPreference("info_cuenta");
        Preference mCambioContraseña = getPreferenceManager().findPreference("cambio_contraseña");

        assert mInfo != null;
        mInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_tucuenta_info);
                return true;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
