package com.csmm.gestorescolar.screens.main.ui.enfermeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.EnfermeriaFragmentBinding;

public class EnfermeriaFragment extends Fragment {

    private EnfermeriaFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EnfermeriaViewModel enfermeriaViewModel =
                new ViewModelProvider(this).get(EnfermeriaViewModel.class);

        binding = EnfermeriaFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEnfermeria;
        enfermeriaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}