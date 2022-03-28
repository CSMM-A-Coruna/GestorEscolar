package com.csmm.gestorescolar.screens.main.ui.cerrarsesion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.csmm.gestorescolar.databinding.FragmentCerrarsesionBinding;
import com.csmm.gestorescolar.screens.auth.LoginActivity;

public class CerrarsesionFragment extends Fragment {

    private FragmentCerrarsesionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CerrarsesionViewModel cerrarsesionViewModel =
                new ViewModelProvider(this).get(CerrarsesionViewModel.class);

        binding = FragmentCerrarsesionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences preferences;
        preferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}