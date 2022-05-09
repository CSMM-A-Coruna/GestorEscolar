package com.csmm.gestorescolar.screens.main.ui.ajustes.detalle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.csmm.gestorescolar.R;
import com.csmm.gestorescolar.client.RestClient;
import com.csmm.gestorescolar.client.handlers.CheckPasswordResponseHandler;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

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

        assert mCambioContraseña != null;
        mCambioContraseña.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Navigation.findNavController(requireView()).navigate(R.id.nav_ajustes_tucuenta_cambiocontraseña);
                return true;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
