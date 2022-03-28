package com.csmm.gestorescolar.screens.main.ui.comunicaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.FragmentComunicacionesBinding;

public class ComunicacionesFragment extends Fragment {

    private FragmentComunicacionesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ComunicacionesViewModel comunicacionesViewModel =
                new ViewModelProvider(this).get(ComunicacionesViewModel.class);

        binding = FragmentComunicacionesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textComunicaciones;
        comunicacionesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}