package com.csmm.gestorescolar.screens.main.ui.entrevistas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.EntrevistasFragmentBinding;

public class EntrevistasFragment extends Fragment {

    private EntrevistasFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EntrevistasViewModel entrevistasViewModel =
                new ViewModelProvider(this).get(EntrevistasViewModel.class);

        binding = EntrevistasFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEntrevistas;
        entrevistasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}