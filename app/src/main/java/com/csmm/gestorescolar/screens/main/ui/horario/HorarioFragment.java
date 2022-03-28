package com.csmm.gestorescolar.screens.main.ui.horario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.FragmentHorarioBinding;

public class HorarioFragment extends Fragment {

    private FragmentHorarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HorarioViewModel horarioViewModel =
                new ViewModelProvider(this).get(HorarioViewModel.class);

        binding = FragmentHorarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHorario;
        horarioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}