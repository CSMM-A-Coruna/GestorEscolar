package com.csmm.gestorescolar.screens.main.ui.calificaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.CalificacionesFragmentBinding;

public class CalificacionesFragment extends Fragment {

    private CalificacionesFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalificacionesViewModel calificacionessViewModel =
                new ViewModelProvider(this).get(CalificacionesViewModel.class);

        binding = CalificacionesFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCalificaciones;
        calificacionessViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}