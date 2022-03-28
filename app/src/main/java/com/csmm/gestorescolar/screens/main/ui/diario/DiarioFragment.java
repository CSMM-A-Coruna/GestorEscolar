package com.csmm.gestorescolar.screens.main.ui.diario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.FragmentDiarioBinding;

public class DiarioFragment extends Fragment {

    private FragmentDiarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DiarioViewModel diarioViewModel =
                new ViewModelProvider(this).get(DiarioViewModel.class);

        binding = FragmentDiarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDiario;
        diarioViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}