package com.csmm.gestorescolar.screens.main.ui.extraescolares;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csmm.gestorescolar.databinding.ExtraescolaresFragmentBinding;

public class ExtraescolaresFragment extends Fragment {

    private ExtraescolaresFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExtraescolaresViewModel extraescolaresViewModel =
                new ViewModelProvider(this).get(ExtraescolaresViewModel.class);

        binding = ExtraescolaresFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textExtraescolares;
        extraescolaresViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}